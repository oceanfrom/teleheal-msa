package thmsa.appointmentservice.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DoctorAvailableRequest(
        UUID doctorId,
        LocalDateTime dateTime
) {
}
