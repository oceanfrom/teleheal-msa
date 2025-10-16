package thmsa.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thmsa.userservice.domain.dto.DoctorProfileRequest;
import thmsa.userservice.domain.dto.DoctorProfileResponse;
import thmsa.userservice.event.DoctorProfileEvent;
import thmsa.userservice.event.EventType;
import thmsa.userservice.exception.DoctorProfileNotFoundException;
import thmsa.userservice.exception.UserNotFoundException;
import thmsa.userservice.mapper.DoctorProfileMapper;
import thmsa.userservice.outbox.OutboxEventPublisher;
import thmsa.userservice.repository.DoctorProfileRepository;
import thmsa.userservice.repository.UserRepository;
import thmsa.userservice.service.DoctorProfileService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IDoctorProfileService implements DoctorProfileService {
    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorProfileMapper doctorProfileMapper;
    private final OutboxEventPublisher outboxEventPublisher;

    @Transactional
    @Override
    public DoctorProfileResponse createProfile(DoctorProfileRequest doctorProfileRequest) {
        var user = userRepository.findById(doctorProfileRequest.userId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + doctorProfileRequest.userId() + " not found"));

        var doctorProfile = doctorProfileMapper.toEntity(doctorProfileRequest);
        doctorProfile.setUser(user);

        outboxEventPublisher.publish(
                DoctorProfileEvent.builder()
                        .doctorId(doctorProfile.getId())
                        .userId(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .specialization(doctorProfile.getSpecialization())
                        .licenseNumber(doctorProfile.getLicenseNumber())
                        .verified(user.isVerifiedDoctor())
                        .eventType(EventType.DOCTOR_CREATED)
                        .build(),
                "DoctorProfile",
                doctorProfile.getId(),
                EventType.DOCTOR_CREATED
        );

        return doctorProfileMapper.toResponse(doctorProfileRepository.save(doctorProfile));
    }

    @Transactional
    @Override
    public DoctorProfileResponse getById(UUID id) {
        var doctorProfile = doctorProfileRepository.findById(id)
                .orElseThrow(() -> new DoctorProfileNotFoundException("User with id " + id + " not found"));
        return doctorProfileMapper.toResponse(doctorProfile);
    }

    @Transactional
    @Override
    public List<DoctorProfileResponse> getAllDoctors() {
        return doctorProfileRepository.findAll()
                .stream()
                .map(doctorProfileMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public List<DoctorProfileResponse> findBySpecialization(String specialization) {
        return  doctorProfileRepository.findBySpecializationIgnoreCase(specialization)
                .stream()
                .map(doctorProfileMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public DoctorProfileResponse updateProfile(DoctorProfileRequest doctorProfileRequest) {
        var doctorProfile = doctorProfileRepository.findById(doctorProfileRequest.id())
                .orElseThrow(() -> new DoctorProfileNotFoundException("Doctor with id " + doctorProfileRequest.id() + " not found"));
        doctorProfile.setSpecialization(doctorProfileRequest.specialization());
        doctorProfile.setLicenseNumber(doctorProfileRequest.licenseNumber());

        outboxEventPublisher.publish(
                DoctorProfileEvent.builder()
                        .doctorId(doctorProfile.getId())
                        .userId(doctorProfile.getId())
                        .name(doctorProfile.getUser().getName())
                        .email(doctorProfile.getUser().getEmail())
                        .specialization(doctorProfile.getSpecialization())
                        .licenseNumber(doctorProfile.getLicenseNumber())
                        .verified(doctorProfile.getUser().isVerifiedDoctor())
                        .eventType(EventType.DOCTOR_UPDATED)
                        .build(),
                "DoctorProfile",
                doctorProfile.getId(),
                EventType.DOCTOR_UPDATED
        );

        return doctorProfileMapper.toResponse(doctorProfileRepository.save(doctorProfile));
    }
}
