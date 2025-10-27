package thmsa.appointmentservice.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DoctorAppointmentsResponse(
        UUID appointmentId,
        LocalDateTime appointmentDate,
        String status,
        UUID patientId
) {}
