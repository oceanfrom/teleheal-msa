package thmsa.appointmentservice.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RescheduleAppointmentDateRequest(
        UUID appointmentId,
        LocalDateTime newAppointmentDate
) {
}
