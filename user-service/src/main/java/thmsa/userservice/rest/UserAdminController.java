package thmsa.userservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thmsa.userservice.domain.dto.DoctorProfileRequest;
import thmsa.userservice.service.DoctorProfileService;

@RestController
@RequestMapping("/api/v1/users/admin")
@RequiredArgsConstructor
public class UserAdminController {
    private final DoctorProfileService doctorProfileService;

    @PostMapping
    public ResponseEntity<?> createDoctorProfile(DoctorProfileRequest doctorProfileRequest) {
        return ResponseEntity.ok(doctorProfileService.createProfile(doctorProfileRequest));
    }

}
