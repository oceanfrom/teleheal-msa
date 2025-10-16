package thmsa.appointmentservice.domain.dto;

import thmsa.appointmentservice.domain.enums.PerformedByType;

import java.util.UUID;

public record AppointmentConfirmRequest(
        UUID appointmentId,
        PerformedByType performedByType
) {
}
