package thmsa.appointmentservice.domain.dto;

import thmsa.appointmentservice.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        UUID doctorId,
        UUID patientId,
        LocalDateTime appointmentDate,
        int appointmentDuration,
        String reason,
        AppointmentStatus appointmentStatus,
        String doctorName,
        String patientName
) {}