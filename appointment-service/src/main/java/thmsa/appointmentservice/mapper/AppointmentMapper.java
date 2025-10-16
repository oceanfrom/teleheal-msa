package thmsa.appointmentservice.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thmsa.appointmentservice.domain.dto.AppointmentRequest;
import thmsa.appointmentservice.domain.dto.AppointmentResponse;
import thmsa.appointmentservice.domain.enums.AppointmentStatus;
import thmsa.appointmentservice.domain.model.Appointment;

@Component
public class AppointmentMapper {
    @Value("${appointment.duration.time}")
    private int APPOINTMENT_DURATION_TIME;

    public Appointment toEntity(AppointmentRequest request) {
        return Appointment.builder()
                .doctorId(request.doctorId())
                .patientId(request.patientId())
                .appointmentDate(request.appointmentDate())
                .appointmentDuration(APPOINTMENT_DURATION_TIME)
                .reason(request.reason())
                .status(AppointmentStatus.SCHEDULED)
                .build();
    }

    public AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDoctorId(),
                appointment.getPatientId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentDuration(),
                appointment.getReason(),
                appointment.getStatus(),
                appointment.getDoctorId().toString(),
                appointment.getPatientId().toString()
        );
    }
}
