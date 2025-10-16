package thmsa.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thmsa.authservice.domain.dto.DoctorProfileRegistrationResponse;
import thmsa.authservice.domain.dto.DoctorRegistrationRequest;
import thmsa.authservice.domain.dto.PatientRegistrationRequest;
import thmsa.authservice.domain.dto.PatientProfileRegistrationResponse;
import thmsa.authservice.domain.enums.UserRole;
import thmsa.authservice.exception.UserWithThisEmailAlreadyExists;
import thmsa.authservice.mapper.AuthMapper;
import thmsa.authservice.repository.UserRepository;
import thmsa.authservice.service.AuthService;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IAuthService implements AuthService {
    private final UserRepository userRepository;
    private final AuthMapper authMapper;

    @Override
    @Transactional
    public PatientProfileRegistrationResponse registerPatient(PatientRegistrationRequest patientRegistrationRequest) {
        if(userRepository.existsByEmail(patientRegistrationRequest.email()))
            throw new UserWithThisEmailAlreadyExists("User with email: " + patientRegistrationRequest.email() + " already exists");

        var user = authMapper.toUserPatientProfile(patientRegistrationRequest);
        user.setPassword(patientRegistrationRequest.password());
        user.setRoles(Collections.singleton(UserRole.PATIENT));
        userRepository.save(user);
        return authMapper.toPatientResponse(user);

    }

    @Override
    @Transactional
    public DoctorProfileRegistrationResponse registerDoctor(DoctorRegistrationRequest doctorRegistrationRequest) {
        if(userRepository.existsByEmail(doctorRegistrationRequest.email()))
            throw new UserWithThisEmailAlreadyExists("User with email: " + doctorRegistrationRequest.email() + " already exists");

        var user = authMapper.toUserDoctorProfile(doctorRegistrationRequest);
        user.setPassword(doctorRegistrationRequest.password());
        user.setRoles(Set.of(UserRole.PATIENT, UserRole.DOCTOR));
        userRepository.save(user);
        return authMapper.toDoctorResponse(user);
    }
}
