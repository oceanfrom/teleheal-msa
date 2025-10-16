package thmsa.appointmentservice.mapper;

import org.springframework.stereotype.Component;
import thmsa.appointmentservice.domain.dto.AppointmentHistoryResponse;
import thmsa.appointmentservice.domain.model.AppointmentHistory;

@Component
public class AppointmentHistoryMapper {
    public AppointmentHistoryResponse toAppointmentHistoryResponse(AppointmentHistory appointmentHistory) {
        return new AppointmentHistoryResponse(
                appointmentHistory.getAppointment().getId(),
                appointmentHistory.getAction(),
                appointmentHistory.getPerformedBy(),
                appointmentHistory.getReason(),
                appointmentHistory.getOldDate(),
                appointmentHistory.getNewDate()
        );
    }

}
