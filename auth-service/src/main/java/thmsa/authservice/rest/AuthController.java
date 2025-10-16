package thmsa.authservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thmsa.authservice.domain.dto.DoctorRegistrationRequest;
import thmsa.authservice.domain.dto.PatientRegistrationRequest;
import thmsa.authservice.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/patients")
    public ResponseEntity<?> createPatientProfile(@RequestBody @Valid PatientRegistrationRequest request) {
        return ResponseEntity.ok(authService.registerPatient(request));
    }

    @PostMapping("/doctors")
    public ResponseEntity<?> createDoctorProfile(@RequestBody @Valid DoctorRegistrationRequest request) {
        return ResponseEntity.ok(authService.registerDoctor(request));
    }

}
