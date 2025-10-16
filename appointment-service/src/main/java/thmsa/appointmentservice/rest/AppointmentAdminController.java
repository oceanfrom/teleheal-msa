package thmsa.appointmentservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thmsa.appointmentservice.domain.dto.AppointmentCancelRequest;
import thmsa.appointmentservice.domain.dto.AppointmentConfirmRequest;
import thmsa.appointmentservice.domain.dto.ReassignDoctorRequest;
import thmsa.appointmentservice.domain.dto.RescheduleAppointmentDateRequest;
import thmsa.appointmentservice.service.AppointmentService;

@RestController
@RequestMapping("/api/v1/appointments/admin")
@RequiredArgsConstructor
public class AppointmentAdminController {
    private final AppointmentService appointmentService;

    @PostMapping("/reassign")
    public ResponseEntity<?> reassignDoctor(ReassignDoctorRequest reassignDoctorRequest) {
        appointmentService.reassignDoctor(reassignDoctorRequest);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/reschedule")
    public ResponseEntity<?> rescheduleAppointment(@RequestBody @Valid RescheduleAppointmentDateRequest rescheduleAppointmentDateRequest) {
        appointmentService.rescheduleAppointment(rescheduleAppointmentDateRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmAppointment(@RequestBody @Valid AppointmentConfirmRequest appointmentConfirmRequest) {
        return ResponseEntity.ok(appointmentService.confirmAppointment(appointmentConfirmRequest));
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelAppointment(@RequestBody @Valid AppointmentCancelRequest appointmentCancelRequest) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentCancelRequest));
    }
}
