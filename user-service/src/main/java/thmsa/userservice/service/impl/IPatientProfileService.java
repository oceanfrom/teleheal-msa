package thmsa.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import thmsa.userservice.domain.dto.PatientProfileRequest;
import thmsa.userservice.domain.model.PatientProfile;
import thmsa.userservice.exception.UserNotFoundException;
import thmsa.userservice.repository.UserRepository;
import thmsa.userservice.service.PatientProfileService;

@Service
@RequiredArgsConstructor
@Slf4j
public class IPatientProfileService implements PatientProfileService {
    private final UserRepository userRepository;

    @Override
    public PatientProfile createPatientProfile(PatientProfileRequest patientProfileRequest) {
        var user = userRepository.findById(patientProfileRequest.userId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + patientProfileRequest.userId() + " not found"));

        var patientProfile = PatientProfile.builder()
                .user(user)
                .emergencyContact(patientProfileRequest.emergencyContact())
                .build();

        return patientProfile;
    }
}
