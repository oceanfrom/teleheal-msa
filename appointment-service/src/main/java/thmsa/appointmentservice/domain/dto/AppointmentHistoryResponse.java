package thmsa.appointmentservice.domain.dto;

import thmsa.appointmentservice.domain.enums.AppointmentStatus;
import thmsa.appointmentservice.domain.enums.PerformedByType;
import thmsa.appointmentservice.domain.model.Appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentHistoryResponse(
        UUID appointmentId,
        AppointmentStatus appointmentStatus,
        PerformedByType performedByType,
        String reason,
        LocalDateTime oldDate,
        LocalDateTime newDate
) {
}
