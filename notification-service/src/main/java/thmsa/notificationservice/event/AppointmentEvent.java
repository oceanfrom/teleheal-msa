package thmsa.notificationservice.event;

import thmsa.notificationservice.domain.enums.AppointmentStatus;
import thmsa.notificationservice.domain.enums.PerformedByType;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentEvent(
        UUID appointmentId,
        UUID doctorId,
        UUID patientId,
        LocalDateTime appointmentDate,
        AppointmentStatus appointmentStatus,
        String eventType,
        PerformedByType performedBy,
        String reason,
        LocalDateTime oldDate // for reschedule
) {}
