package thmsa.appointmentservice.mapper;

import org.springframework.stereotype.Component;
import thmsa.appointmentservice.domain.dto.DoctorAppointmentsResponse;
import thmsa.appointmentservice.domain.model.Appointment;

@Component
public class DoctorAppointmentsMapper {
    public DoctorAppointmentsResponse toResponse(Appointment appointment) {
        return new DoctorAppointmentsResponse(
                appointment.getId(),
                appointment.getAppointmentDate(),
                appointment.getStatus().name(),
                appointment.getPatientId()
        );
    }
}
