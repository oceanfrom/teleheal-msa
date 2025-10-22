package thmsa.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thmsa.userservice.domain.dto.PatientProfileRequest;
import thmsa.userservice.domain.dto.PatientProfileResponse;
import thmsa.userservice.domain.model.PatientProfile;
import thmsa.userservice.exception.PatientProfileNotFoundException;
import thmsa.userservice.exception.UserNotFoundException;
import thmsa.userservice.mapper.PatientProfileMapper;
import thmsa.userservice.repository.PatientProfileRepository;
import thmsa.userservice.repository.UserRepository;
import thmsa.userservice.service.PatientProfileService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IPatientProfileService implements PatientProfileService {
    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final PatientProfileMapper patientProfileMapper;

    @Transactional
    @Override
    public PatientProfileResponse createPatientProfile(PatientProfileRequest patientProfileRequest) {
        var user = userRepository.findById(patientProfileRequest.userId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + patientProfileRequest.userId() + " not found"));

        var patientProfile = PatientProfile.builder()
                .user(user)
                .emergencyContact(patientProfileRequest.emergencyContact())
                .build();

        return patientProfileMapper.toResponse(patientProfile);
    }

    @Transactional
    @Override
    public PatientProfileResponse updatePatient(PatientProfileRequest patientProfileRequest) {
        var patientProfile = patientProfileRepository.findById(patientProfileRequest.id())
                .orElseThrow(() -> new PatientProfileNotFoundException("Patient with id " + patientProfileRequest.id() + " not found"));

        patientProfile.setEmergencyContact(patientProfileRequest.emergencyContact());

        return patientProfileMapper.toResponse(patientProfileRepository.save(patientProfile));
    }

    @Transactional(readOnly = true)
    @Override
    public PatientProfileResponse getPatientById(UUID patientId) {
        var patientProfile = patientProfileRepository.findById(patientId)
                .orElseThrow(() -> new PatientProfileNotFoundException("Patient with id " + patientId + " not found"));

        return patientProfileMapper.toResponse(patientProfile);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PatientProfileResponse> getAllPatients() {
        return patientProfileRepository.findAll()
                .stream()
                .map(patientProfileMapper::toResponse)
                .toList();
    }
}
