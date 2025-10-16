package thmsa.appointmentservice.service;

import thmsa.appointmentservice.domain.dto.*;
import thmsa.appointmentservice.domain.enums.PerformedByType;
import thmsa.appointmentservice.domain.model.Appointment;

import java.util.UUID;

public interface AppointmentService {
    AppointmentResponse getAppointmentById(UUID id);
    AppointmentResponse createAppointment(AppointmentRequest appointmentRequest);

    AppointmentResponse confirmAppointment(AppointmentConfirmRequest appointmentConfirmRequest);

    AppointmentResponse cancelAppointment(AppointmentCancelRequest appointmentCancelRequest);

    void rescheduleAppointment(RescheduleAppointmentDateRequest rescheduleAppointmentDateRequest);
    void reassignDoctor(ReassignDoctorRequest reassignDoctorRequest);
}
