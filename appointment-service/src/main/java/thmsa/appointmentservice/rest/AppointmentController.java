package thmsa.appointmentservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thmsa.appointmentservice.domain.dto.*;
import thmsa.appointmentservice.service.AppointmentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getAppointmentByPatientId(@PathVariable UUID patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId));
    }

    @GetMapping("/patient/{patientId}/upcoming")
    public ResponseEntity<?> getUpcomingAppointments(@PathVariable UUID patientId) {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointments(patientId));
    }

    @GetMapping("/{doctorId}/schedule")
    public ResponseEntity<?> getAppointmentByDoctorId(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(doctorId));
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody @Valid AppointmentRequest appointmentRequest) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointmentRequest));
    }
}
