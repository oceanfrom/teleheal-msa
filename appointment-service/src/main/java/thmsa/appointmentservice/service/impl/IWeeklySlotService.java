package thmsa.appointmentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thmsa.appointmentservice.domain.dto.*;
import thmsa.appointmentservice.domain.model.WeeklySlot;
import thmsa.appointmentservice.exception.AppointmentOverlappingException;
import thmsa.appointmentservice.exception.InvalidAppointmentTimeException;
import thmsa.appointmentservice.exception.WeeklySlotException;
import thmsa.appointmentservice.mapper.WeeklySlotMapper;
import thmsa.appointmentservice.repository.AppointmentRepository;
import thmsa.appointmentservice.repository.WeeklySlotRepository;
import thmsa.appointmentservice.service.WeeklySlotService;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class    IWeeklySlotService implements WeeklySlotService {

    private final WeeklySlotRepository weeklySlotRepository;
    private final AppointmentRepository appointmentRepository;
    private final WeeklySlotMapper weeklySlotMapper;

    @Value("${appointment.duration.time}")
    private int APPOINTMENT_DURATION_TIME;

    @Transactional
    @Override
    public boolean isSlotAvailable(UUID doctorId, LocalDateTime appointmentDate) {
        List<WeeklySlot> weeklySlots = weeklySlotRepository.findByDoctorId(doctorId);

        return weeklySlots.stream()
                .anyMatch(slot ->
                        slot.getDayOfWeek() == appointmentDate.getDayOfWeek() &&
                                !appointmentDate.toLocalTime().isBefore(slot.getStartTime()) &&
                                !appointmentDate.toLocalTime().plusMinutes(APPOINTMENT_DURATION_TIME).isAfter(slot.getEndTime())
                );
    }


    @Transactional(readOnly = true)
    @Override
    public boolean isDoctorAvailableAt(UUID doctorId, LocalDateTime dateTime) {
        var date = dateTime.toLocalDate();
        var startTime = dateTime.toLocalTime();
        var endTime = startTime.plusMinutes(APPOINTMENT_DURATION_TIME);

        var dateOverride = weeklySlotRepository.findByDoctorIdAndDateOverride(doctorId, date);
        if (dateOverride.stream().anyMatch(slot ->
                slot.isActive() &&
                        !startTime.isBefore(slot.getStartTime()) &&
                        !endTime.isAfter(slot.getEndTime())
        )) return true;

        var day = date.getDayOfWeek();
        var weekly = weeklySlotRepository.findByDoctorIdAndDayOfWeek(doctorId, day);
        return weekly.stream().anyMatch(slot ->
                slot.isActive() &&
                        !startTime.isBefore(slot.getStartTime()) &&
                        !endTime.isAfter(slot.getEndTime())
        );
    }

    @Transactional
    @Override
    public List<WeeklySlotResponse> createDailySlots(DailySlotCreateRequest request) {
        WeeklySlot baseSlot = weeklySlotMapper.toEntity(request);
        validateTime(baseSlot.getStartTime(), baseSlot.getEndTime());
        checkOverlap(baseSlot.getDoctorId(), baseSlot.getDayOfWeek(),
                baseSlot.getStartTime(), baseSlot.getEndTime(), baseSlot.getDateOverride());

        List<WeeklySlot> slots = generateSlotsFromEntity(baseSlot);
        return weeklySlotMapper.toResponseList(weeklySlotRepository.saveAll(slots));
    }

    @Transactional
    @Override
    public List<WeeklySlotResponse> createWeeklySlots(WeeklySlotsCreateRequest request) {
        List<WeeklySlot> allSlots = new ArrayList<>();
        for (WeeklySlotRequest slotReq : request.slots()) {
            LocalDate slotDate = request.weekStart().with(DayOfWeek.MONDAY)
                    .plusDays(slotReq.dayOfWeek().getValue() - 1);

            WeeklySlot baseSlot = weeklySlotMapper.toEntity(slotReq, slotDate, request.doctorId());
            validateTime(baseSlot.getStartTime(), baseSlot.getEndTime());
            checkOverlap(baseSlot.getDoctorId(), baseSlot.getDayOfWeek(),
                    baseSlot.getStartTime(), baseSlot.getEndTime(), slotDate);

            allSlots.addAll(generateSlotsFromEntity(baseSlot));
        }
        return weeklySlotMapper.toResponseList(weeklySlotRepository.saveAll(allSlots));
    }

    @Transactional
    @Override
    public List<WeeklySlotResponse> updateSlotsByRange(SlotRangeUpdateRequest request) {
        var from = request.fromDate();
        var to = request.toDate();
        var startTime = request.fromTime() != null ? request.fromTime() : LocalTime.MIN;
        var endTime = request.toTime() != null ? request.toTime() : LocalTime.MAX;

        var slotsInRange = weeklySlotRepository.findAllByDoctorIdAndDateOverrideBetween(
                request.doctorId(),
                from,
                to
        );

        var updatedSlots = new ArrayList<WeeklySlot>();

        for (var currentDate = from; !currentDate.isAfter(to); currentDate = currentDate.plusDays(1)) {
            var dateCopy = currentDate;

            var slotsForDate = slotsInRange.stream()
                    .filter(slot -> dateCopy.equals(slot.getDateOverride()))
                    .collect(Collectors.toList());

            if (Boolean.TRUE.equals(request.delete())) {
                updatedSlots.addAll(deleteSlotsForDate(startTime, endTime, slotsForDate));
            } else {
                updatedSlots.addAll(updateOrCreateSlotsForDate(request, dateCopy, startTime, endTime, slotsForDate));
            }
        }


        return weeklySlotMapper.toResponseList(weeklySlotRepository.saveAll(updatedSlots));
    }

    private void checkOverlap(UUID doctorId, DayOfWeek dayOfWeek, LocalTime startTime,
                              LocalTime endTime, LocalDate dateOverride) {
        List<WeeklySlot> overlappingSlots;
        if (dateOverride != null) {
            overlappingSlots = weeklySlotRepository.findByDoctorIdAndDateOverrideAndStartTimeLessThanAndEndTimeGreaterThan(
                    doctorId, dateOverride, endTime, startTime);
        } else {
            overlappingSlots = weeklySlotRepository.findByDoctorIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
                    doctorId, dayOfWeek, endTime, startTime);
        }
        if (!overlappingSlots.isEmpty()) {
            throw new AppointmentOverlappingException("Slot overlaps with existing slot");
        }
    }

    private void validateTime(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime))
            throw new InvalidAppointmentTimeException("Start time must be before end time");
    }

    private List<WeeklySlot> generateSlotsFromEntity(WeeklySlot baseSlot) {
        List<WeeklySlot> slots = new ArrayList<>();
        LocalTime current = baseSlot.getStartTime();
        while (!current.plusMinutes(APPOINTMENT_DURATION_TIME).isAfter(baseSlot.getEndTime())) {
            slots.add(WeeklySlot.builder()
                    .doctorId(baseSlot.getDoctorId())
                    .dayOfWeek(baseSlot.getDayOfWeek())
                    .dateOverride(baseSlot.getDateOverride())
                    .startTime(current)
                    .endTime(current.plusMinutes(APPOINTMENT_DURATION_TIME))
                    .isActive(true)
                    .build());
            current = current.plusMinutes(APPOINTMENT_DURATION_TIME);
        }
        return slots;
    }

    private boolean hasFutureAppointments(WeeklySlot slot) {
        LocalDate date = resolveSlotDate(slot);
        LocalDateTime slotStart = LocalDateTime.of(date, slot.getStartTime());
        return appointmentRepository.existsByDoctorIdAndAppointmentDateAfter(slot.getDoctorId(), slotStart);
    }

    private LocalDate resolveSlotDate(WeeklySlot slot) {
        if (slot.getDateOverride() != null) return slot.getDateOverride();
        var today = LocalDate.now();
        int currentDay = today.getDayOfWeek().getValue();
        int slotDay = slot.getDayOfWeek().getValue();
        int daysUntil = (slotDay - currentDay + 7) % 7;
        return today.plusDays(daysUntil);
    }

    private List<WeeklySlot> deleteSlotsForDate(
                                                LocalTime startTime, LocalTime endTime,
                                                List<WeeklySlot> slotsForDate) {
        List<WeeklySlot> toDelete = slotsForDate.stream()
                .filter(slot -> !slot.getStartTime().isBefore(startTime) && !slot.getEndTime().isAfter(endTime))
                .collect(Collectors.toList());

        for (WeeklySlot slot : toDelete) {
            if (hasFutureAppointments(slot)) {
                throw new WeeklySlotException("Cannot delete slot with future appointments: " + slot.getId());
            }
        }

        weeklySlotRepository.deleteAll(toDelete);
        return toDelete;
    }

    private List<WeeklySlot> updateOrCreateSlotsForDate(SlotRangeUpdateRequest request, LocalDate date,
                                                        LocalTime startTime, LocalTime endTime,
                                                        List<WeeklySlot> slotsForDate) {
        for (WeeklySlot slot : slotsForDate) {
            if (Boolean.TRUE.equals(request.isActive()) && !slot.isActive()) slot.setActive(true);
            else if (Boolean.FALSE.equals(request.isActive()) && slot.isActive()) {
                if (hasFutureAppointments(slot))
                    throw new WeeklySlotException("Cannot deactivate slot with future appointments: " + slot.getId());
                slot.setActive(false);
            }
        }

        if (Boolean.TRUE.equals(request.isActive()) && slotsForDate.isEmpty()) {
            WeeklySlot baseSlot = WeeklySlot.builder()
                    .doctorId(request.doctorId())
                    .dayOfWeek(date.getDayOfWeek())
                    .dateOverride(date)
                    .startTime(startTime)
                    .endTime(endTime)
                    .isActive(true)
                    .build();
            slotsForDate.addAll(generateSlotsFromEntity(baseSlot));
        }

        return slotsForDate;
    }
}
