package thmsa.appointmentservice.domain.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record WeeklySlotsCreateRequest(
        UUID doctorId,
        LocalDate weekStart,
        List<WeeklySlotRequest> slots
) {}