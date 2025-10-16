package thmsa.userservice.mapper;

import org.springframework.stereotype.Component;
import thmsa.userservice.domain.dto.DoctorProfileRequest;
import thmsa.userservice.domain.dto.DoctorProfileResponse;
import thmsa.userservice.domain.model.DoctorProfile;

@Component
public class DoctorProfileMapper {
    public DoctorProfile toEntity(DoctorProfileRequest doctorProfileRequest) {
        return DoctorProfile.builder()
                .specialization(doctorProfileRequest.specialization())
                .licenseNumber(doctorProfileRequest.licenseNumber())
                .build();
    }

    public DoctorProfileResponse toResponse(DoctorProfile profile) {
        return new DoctorProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getUser().getName(),
                profile.getUser().getEmail(),
                profile.getSpecialization(),
                profile.getLicenseNumber(),
                profile.getUser().isVerifiedDoctor()
        );
    }
}
