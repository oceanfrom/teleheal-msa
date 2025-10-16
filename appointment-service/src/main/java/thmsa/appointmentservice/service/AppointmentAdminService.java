package thmsa.appointmentservice.service;

import thmsa.appointmentservice.domain.dto.RescheduleAppointmentDateRequest;
import thmsa.appointmentservice.domain.dto.ReassignDoctorRequest;

public interface AppointmentAdminService {
    void reassignDoctor(ReassignDoctorRequest reassignDoctorRequest);
    void RescheduleAppointmentDate(RescheduleAppointmentDateRequest rescheduleAppointmentDateRequest);
}
