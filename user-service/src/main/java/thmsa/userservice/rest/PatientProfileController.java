package thmsa.userservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thmsa.userservice.domain.dto.PatientProfileRequest;
import thmsa.userservice.service.PatientProfileService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientProfileController {
    private final PatientProfileService patientProfileService;

    @PostMapping
    ResponseEntity<?> createPatientProfile(@RequestBody @Valid PatientProfileRequest patientProfileRequest) {
        return ResponseEntity.ok(patientProfileService.createPatientProfile(patientProfileRequest));
    }

    @GetMapping
    ResponseEntity<?> getAllPatientProfiles() {
        return ResponseEntity.ok(patientProfileService.getAllPatients());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getPatientProfileById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(patientProfileService.getPatientById(id));
    }

    @GetMapping("/{id}/history")
    ResponseEntity<?> getPatientHistory(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(patientProfileService.getPatientHistory(id));
    }

    @GetMapping("/{id}/upcoming")
    public ResponseEntity<?> getPatientUpcomingAppointments(@PathVariable UUID id) {
        return ResponseEntity.ok(patientProfileService.getPatientUpcomingAppointments(id));
    }

    @PutMapping("/update")
    ResponseEntity<?> updatePatientProfile(@RequestBody @Valid PatientProfileRequest patientProfileRequest) {
        return ResponseEntity.ok(patientProfileService.updatePatient(patientProfileRequest));
    }

}
