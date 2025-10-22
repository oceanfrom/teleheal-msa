package thmsa.userservice.mapper;

import org.springframework.stereotype.Component;
import thmsa.userservice.domain.dto.PatientProfileRequest;
import thmsa.userservice.domain.dto.PatientProfileResponse;
import thmsa.userservice.domain.model.PatientProfile;
import thmsa.userservice.domain.model.User;

@Component
public class PatientProfileMapper {
    public PatientProfile toEntity(PatientProfileRequest request, User user) {
        return PatientProfile.builder()
                .user(user)
                .emergencyContact(request.emergencyContact())
                .build();
    }

    public PatientProfileResponse toResponse(PatientProfile patientProfile) {
        var user = patientProfile.getUser();
        return new PatientProfileResponse(
                patientProfile.getId(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                patientProfile.getEmergencyContact()
        );
    }
}
