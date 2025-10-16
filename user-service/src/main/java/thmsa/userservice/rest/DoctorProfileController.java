package thmsa.userservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thmsa.userservice.domain.dto.DoctorProfileRequest;
import thmsa.userservice.service.DoctorProfileService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorProfileController {

    private final DoctorProfileService doctorProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(doctorProfileService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<?>> getAllBySpecialization(@RequestParam(required = false) String specialization) {
        if (specialization != null) {
            return ResponseEntity.ok(doctorProfileService.findBySpecialization(specialization));
        }
        return ResponseEntity.ok(doctorProfileService.getAllDoctors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody DoctorProfileRequest doctorProfileRequest) {
        return ResponseEntity.ok(doctorProfileService.updateProfile(doctorProfileRequest));
    }
}
