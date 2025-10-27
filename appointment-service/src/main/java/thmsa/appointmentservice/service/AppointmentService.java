package thmsa.appointmentservice.service;

import thmsa.appointmentservice.domain.dto.*;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    AppointmentResponse getAppointmentById(UUID id);

    List<AppointmentResponse> getAppointmentsByPatientId(UUID patientId);

    List<DoctorAppointmentsResponse> getAppointmentsByDoctorId(UUID doctorId);

    AppointmentResponse createAppointment(AppointmentRequest appointmentRequest);

    AppointmentResponse confirmAppointment(AppointmentConfirmRequest appointmentConfirmRequest);

    void autoConfirmUnconfirmedAppointments();

    AppointmentResponse cancelAppointment(AppointmentCancelRequest appointmentCancelRequest);

    void rescheduleAppointment(RescheduleAppointmentDateRequest rescheduleAppointmentDateRequest);

    void reassignDoctor(ReassignDoctorRequest reassignDoctorRequest);

    List<AppointmentResponse> getUpcomingAppointments(UUID patientId);

    void sendUpcomingAppointmentReminders();
}
