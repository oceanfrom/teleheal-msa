package thmsa.appointmentservice.mapper;

import org.springframework.stereotype.Component;
import thmsa.appointmentservice.domain.dto.DailySlotCreateRequest;
import thmsa.appointmentservice.domain.dto.WeeklySlotRequest;
import thmsa.appointmentservice.domain.dto.WeeklySlotResponse;
import thmsa.appointmentservice.domain.model.WeeklySlot;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WeeklySlotMapper {

    public WeeklySlot toEntity(DailySlotCreateRequest request) {
        return WeeklySlot.builder()
                .doctorId(request.doctorId())
                .dayOfWeek(request.dayOfWeek())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .isActive(true)
                .dateOverride(request.dateOverride())
                .build();
    }

    // for months
    public WeeklySlot toEntity(WeeklySlotRequest request, LocalDate dateOverride, UUID doctorId) {
        return WeeklySlot.builder()
                .doctorId(doctorId)
                .dayOfWeek(request.dayOfWeek())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .isActive(true)
                .dateOverride(dateOverride)
                .build();
    }


    public WeeklySlotResponse toResponse(WeeklySlot slot) {
        return new WeeklySlotResponse(
                slot.getId(),
                slot.getDoctorId(),
                slot.getDayOfWeek(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.isActive(),
                slot.getDateOverride()
        );
    }

    public List<WeeklySlotResponse> toResponseList(List<WeeklySlot> slots) {
        return slots.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

