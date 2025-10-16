package thmsa.appointmentservice.domain.dto;

import thmsa.appointmentservice.domain.enums.PerformedByType;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRescheduleRequest(
        UUID appointmentId,
        LocalDateTime newDate,
        PerformedByType performedBy,
        String reason
) {}