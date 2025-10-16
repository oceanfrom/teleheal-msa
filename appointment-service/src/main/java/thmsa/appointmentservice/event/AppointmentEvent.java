package thmsa.appointmentservice.event;

import thmsa.appointmentservice.domain.enums.AppointmentStatus;
import thmsa.appointmentservice.domain.enums.PerformedByType;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentEvent(
        UUID appointmentId,
        UUID doctorId,
        UUID newDoctorId,
        UUID patientId,
        LocalDateTime appointmentDate,
        AppointmentStatus appointmentStatus,
        EventType eventType,
        PerformedByType performedBy,
        String reason,
        LocalDateTime oldDate // for reschedule
) {}
