package thmsa.appointmentservice.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequest(
        UUID doctorId,
        UUID patientId,
        LocalDateTime appointmentDate,
        String reason
) {}