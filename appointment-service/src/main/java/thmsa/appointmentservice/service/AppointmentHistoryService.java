package thmsa.appointmentservice.service;

import thmsa.appointmentservice.domain.dto.AppointmentHistoryRequest;
import thmsa.appointmentservice.domain.dto.AppointmentHistoryResponse;
import thmsa.appointmentservice.domain.enums.AppointmentStatus;
import thmsa.appointmentservice.domain.enums.PerformedByType;
import thmsa.appointmentservice.domain.model.Appointment;

import java.time.LocalDateTime;

public interface AppointmentHistoryService {
    public AppointmentHistoryResponse record(Appointment appointment,
                                             AppointmentStatus appointmentStatus,
                                             PerformedByType performedByType,
                                             String reason,
                                             LocalDateTime oldDate,
                                             LocalDateTime newDate);
}
