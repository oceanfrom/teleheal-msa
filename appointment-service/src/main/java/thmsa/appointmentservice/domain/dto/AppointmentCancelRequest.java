package thmsa.appointmentservice.domain.dto;

import thmsa.appointmentservice.domain.enums.PerformedByType;

import java.util.UUID;

public record AppointmentCancelRequest(
        UUID appointmentId,
        PerformedByType performedBy,
        String reason
) {
}