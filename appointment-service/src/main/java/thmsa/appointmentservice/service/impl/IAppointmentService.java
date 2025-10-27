package thmsa.appointmentservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thmsa.appointmentservice.domain.dto.*;
import thmsa.appointmentservice.domain.enums.AppointmentStatus;
import thmsa.appointmentservice.domain.enums.PerformedByType;
import thmsa.appointmentservice.event.AppointmentEvent;
import thmsa.appointmentservice.event.EventType;
import thmsa.appointmentservice.exception.AppointmentAlreadyExistsException;
import thmsa.appointmentservice.exception.AppointmentStatusException;
import thmsa.appointmentservice.exception.InvalidAppointmentTimeException;
import thmsa.appointmentservice.exception.AppointmentNotFondException;
import thmsa.appointmentservice.mapper.AppointmentMapper;
import thmsa.appointmentservice.mapper.DoctorAppointmentsMapper;
import thmsa.appointmentservice.outbox.OutboxEventPublisher;
import thmsa.appointmentservice.repository.AppointmentRepository;
import thmsa.appointmentservice.service.AppointmentHistoryService;
import thmsa.appointmentservice.service.AppointmentService;
import thmsa.appointmentservice.service.WeeklySlotService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IAppointmentService implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final WeeklySlotService weeklySlotService;
    private final AppointmentHistoryService appointmentHistoryService;
    private final OutboxEventPublisher outboxEventPublisher;
    private final AppointmentMapper appointmentMapper;
    private final DoctorAppointmentsMapper doctorAppointmentsMapper;

    @Value("${appointment.auto-confirm-after-hour")
    private int AUTO_CONFIRM_AFTER_HOURS;


    @Transactional
    @Override
    public AppointmentResponse getAppointmentById(UUID id) {
        var appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFondException("Appointment with id " + id + " not found"));
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentResponse> getAppointmentsByPatientId(UUID patientId) {
        return appointmentRepository.findAppointmentsByPatientId(patientId)
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorAppointmentsResponse> getAppointmentsByDoctorId(UUID doctorId) {
        return appointmentRepository.findAllByDoctorId(doctorId)
                .stream()
                .map(doctorAppointmentsMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {
        if (appointmentRepository.existsByDoctorIdAndAppointmentDate(
                appointmentRequest.doctorId(),
                appointmentRequest.appointmentDate())
        )
            throw new AppointmentAlreadyExistsException("Appointment already exists for doctor " + appointmentRequest.doctorId() + " at " + appointmentRequest.appointmentDate());

        var isSlotAvailableAt = weeklySlotService.isSlotAvailable(
                appointmentRequest.doctorId(),
                appointmentRequest.appointmentDate()
        );
        if (!isSlotAvailableAt)
            throw new InvalidAppointmentTimeException("Doctor " + appointmentRequest.doctorId() + " is not available at " + appointmentRequest.appointmentDate());

        var isDoctorAvailableAt = weeklySlotService.isDoctorAvailableAt(
                appointmentRequest.doctorId(),
                appointmentRequest.appointmentDate()
        );
        if (!isDoctorAvailableAt)
            throw new InvalidAppointmentTimeException("Doctor " + appointmentRequest.doctorId() + " is not available at " + appointmentRequest.appointmentDate());

        var appointment = appointmentRepository.save(appointmentMapper.toEntity(appointmentRequest));
        appointmentHistoryService.record(appointment,
                appointment.getStatus(),
                PerformedByType.PATIENT,
                AppointmentStatus.SCHEDULED.toString(),
                null,
                appointment.getAppointmentDate()
        );

        outboxEventPublisher.publish(
                new AppointmentEvent(
                        appointment.getId(),
                        appointment.getDoctorId(),
                        null,
                        appointment.getPatientId(),
                        appointment.getAppointmentDate(),
                        appointment.getStatus(),
                        EventType.CREATED,
                        PerformedByType.PATIENT,
                        null,
                        null
                ),
                "Appointment",
                appointment.getId(),
                EventType.CREATED
        );
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    @Override
    public AppointmentResponse confirmAppointment(AppointmentConfirmRequest appointmentConfirmRequest) {
        var appointment = appointmentRepository.findById(appointmentConfirmRequest.appointmentId())
                .orElseThrow(() -> new AppointmentNotFondException("Appointment with id " + appointmentConfirmRequest.appointmentId() + " not found"));

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED)
            throw new AppointmentStatusException("Only Scheduled appointments can be confirmed");

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);
        appointmentHistoryService.record(appointment,
                AppointmentStatus.CONFIRMED,
                appointmentConfirmRequest.performedByType(),
                null,
                null,
                null
        );

        outboxEventPublisher.publish(
                new AppointmentEvent(
                        appointment.getId(),
                        appointment.getDoctorId(),
                        null,
                        appointment.getPatientId(),
                        appointment.getAppointmentDate(),
                        appointment.getStatus(),
                        EventType.CONFIRMED,
                        appointmentConfirmRequest.performedByType(),
                        null,
                        null
                ),
                "Appointment",
                appointment.getId(),
                EventType.CONFIRMED
        );
        return appointmentMapper.toResponse(appointment);
    }

    @Scheduled(fixedRate = 30 * 60 * 1000)
    @Transactional
    @Override
    public void autoConfirmUnconfirmedAppointments() {
        var trashHold = LocalDateTime.now().minusHours(AUTO_CONFIRM_AFTER_HOURS);
        var pendingAppointments = appointmentRepository.findAllByStatusAndCreatedAtBefore(AppointmentStatus.SCHEDULED, trashHold);

        if (pendingAppointments.isEmpty()) return;

        pendingAppointments.forEach(appointment -> {
                    appointment.setStatus(AppointmentStatus.CONFIRMED);
                    appointmentRepository.save(appointment);

                    appointmentHistoryService.record(
                            appointment,
                            AppointmentStatus.CONFIRMED,
                            PerformedByType.SYSTEM,
                            "Automatically confirmed after waiting period",
                            null,
                            null
                    );

                    outboxEventPublisher.publish(
                            new AppointmentEvent(
                                    appointment.getId(),
                                    appointment.getDoctorId(),
                                    null,
                                    appointment.getPatientId(),
                                    appointment.getAppointmentDate(),
                                    AppointmentStatus.CONFIRMED,
                                    EventType.CONFIRMED,
                                    PerformedByType.SYSTEM,
                                    "Auto-confirmed by scheduler",
                                    null
                            ),
                            "Appointment",
                            appointment.getId(),
                            EventType.CONFIRMED
                    );
                }
        );
    }

    @Transactional
    @Override
    public AppointmentResponse cancelAppointment(AppointmentCancelRequest appointmentCancelRequest) {
        var appointment = appointmentRepository.findById(appointmentCancelRequest.appointmentId())
                .orElseThrow(() -> new AppointmentNotFondException("Appointment with id " + appointmentCancelRequest.appointmentId() + " not found"));
        if (appointment.getStatus() == AppointmentStatus.COMPLETED)
            throw new AppointmentStatusException("Cannot cancel completed appointment");

        appointment.setStatus(appointmentCancelRequest.performedBy() == PerformedByType.DOCTOR
                ? AppointmentStatus.CANCELLED_BY_DOCTOR : AppointmentStatus.CANCELLED_BY_PATIENT);
        appointmentRepository.save(appointment);
        appointmentHistoryService.record(appointment,
                appointment.getStatus(),
                appointmentCancelRequest.performedBy(),
                appointmentCancelRequest.reason(),
                appointment.getAppointmentDate(),
                null
        );

        outboxEventPublisher.publish(
                new AppointmentEvent(
                        appointment.getId(),
                        appointment.getDoctorId(),
                        null,
                        appointment.getPatientId(),
                        appointment.getAppointmentDate(),
                        appointment.getStatus(),
                        EventType.CANCELLED,
                        appointmentCancelRequest.performedBy(),
                        appointmentCancelRequest.reason(),
                        appointment.getAppointmentDate()
                ),
                "Appointment",
                appointment.getId(),
                EventType.CANCELLED
        );
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    @Override
    public void rescheduleAppointment(RescheduleAppointmentDateRequest rescheduleAppointmentDateRequest) {
        var appointment = appointmentRepository.findById(rescheduleAppointmentDateRequest.appointmentId())
                .orElseThrow(() -> new AppointmentNotFondException("Appointment not found"));

        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now().plusHours(12))) { // add here yml value later..
            throw new IllegalStateException("Cannot reassign appointment less than 3 hours before scheduled time");
        }

        var isSlotAvailable = weeklySlotService.isSlotAvailable(appointment.getDoctorId(), rescheduleAppointmentDateRequest.newAppointmentDate());

        if (!isSlotAvailable) {
            throw new IllegalStateException("New doctor is not available at the requested time");
        }

        var oldDate = appointment.getAppointmentDate();

        appointment.setAppointmentDate(rescheduleAppointmentDateRequest.newAppointmentDate());
        appointmentRepository.save(appointment);

        appointmentHistoryService.record(
                appointment,
                AppointmentStatus.RESCHEDULED,
                PerformedByType.SYSTEM,
                "Appointment rescheduled at" + rescheduleAppointmentDateRequest.newAppointmentDate(),
                oldDate,
                rescheduleAppointmentDateRequest.newAppointmentDate()
        );

        outboxEventPublisher.publish(
                new AppointmentEvent(
                        appointment.getId(),
                        appointment.getDoctorId(),
                        null,
                        appointment.getPatientId(),
                        oldDate,
                        AppointmentStatus.RESCHEDULED,
                        EventType.RESCHEDULED,
                        PerformedByType.SYSTEM,
                        "Reason",
                        null
                ),
                "Appointment",
                appointment.getId(),
                EventType.RESCHEDULED
        );
    }

    @Transactional
    @Override
    public void reassignDoctor(ReassignDoctorRequest reassignDoctorRequest) {
        var appointment = appointmentRepository.findById(reassignDoctorRequest.appointmentId())
                .orElseThrow(() -> new AppointmentNotFondException("Appointment not found"));

        var oldDoctorId = appointment.getDoctorId();

        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now().plusHours(12)))
            throw new IllegalStateException("Cannot reassign doctor less than 3 hours before appointment");

        var isDoctorAvailable = weeklySlotService.isDoctorAvailableAt(reassignDoctorRequest.newDoctorId(), appointment.getAppointmentDate());
        var isSlotAvailable = weeklySlotService.isSlotAvailable(reassignDoctorRequest.newDoctorId(), appointment.getAppointmentDate());

        if (isDoctorAvailable && isSlotAvailable) {
            appointment.setDoctorId(reassignDoctorRequest.newDoctorId());
            appointmentRepository.save(appointment);
            appointmentHistoryService.record(
                    appointment,
                    null,
                    PerformedByType.SYSTEM,
                    "Reassign Doctor",
                    null,
                    null
            );

            outboxEventPublisher.publish(
                    new AppointmentEvent(
                            oldDoctorId,
                            appointment.getDoctorId(),
                            reassignDoctorRequest.newDoctorId(),
                            appointment.getPatientId(),
                            appointment.getAppointmentDate(),
                            appointment.getStatus(),
                            EventType.RESCHEDULED,
                            PerformedByType.SYSTEM,
                            "Reassign Doctor",
                            null
                    ),
                    "Appointment",
                    appointment.getId(),
                    EventType.RESCHEDULED
            );
        }
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getUpcomingAppointments(UUID patientId) {
        List<AppointmentStatus> activeStatuses = List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED);

        return appointmentRepository
                .findTop5ByPatientIdAndStatusInOrderByAppointmentDateAsc(patientId, activeStatuses)
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Transactional
    @Scheduled(fixedRate = 15 * 60 * 1000)
    @Override
    public void sendUpcomingAppointmentReminders() {
        var now = LocalDateTime.now();
        var twoHoursLater = now.plusHours(2);

        var upcomingAppointments = appointmentRepository.findAppointmentsWithinNextHours(
                now,
                twoHoursLater,
                Arrays.asList(AppointmentStatus.RESCHEDULED, AppointmentStatus.CONFIRMED)
        );

        if (upcomingAppointments.isEmpty()) return;

        for (var appointment : upcomingAppointments) {
            var event = new AppointmentEvent(
                    appointment.getId(),
                    appointment.getDoctorId(),
                    null,
                    appointment.getPatientId(),
                    appointment.getAppointmentDate(),
                    appointment.getStatus(),
                    EventType.UPCOMING,
                    PerformedByType.SYSTEM,
                    null,
                    null);

            outboxEventPublisher.publish(event, "Appointment", appointment.getId(), EventType.UPCOMING);
        }
    }

}