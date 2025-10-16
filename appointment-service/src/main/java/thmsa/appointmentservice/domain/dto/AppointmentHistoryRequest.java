package thmsa.appointmentservice.domain.dto;

import thmsa.appointmentservice.domain.enums.AppointmentStatus;
import thmsa.appointmentservice.domain.enums.PerformedByType;
import thmsa.appointmentservice.domain.model.Appointment;

import java.time.LocalDateTime;

public record AppointmentHistoryRequest(
        Appointment appointment,
        AppointmentStatus appointmentStatus,
        PerformedByType performedByType,
        String reason,
        LocalDateTime oldDate,
        LocalDateTime newDate
) {}