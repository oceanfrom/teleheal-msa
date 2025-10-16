package thmsa.userservice.service;

import thmsa.userservice.domain.dto.DoctorProfileRequest;
import thmsa.userservice.domain.dto.DoctorProfileResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorProfileService {
    DoctorProfileResponse createProfile(DoctorProfileRequest request);
    DoctorProfileResponse getById(UUID id);
    List<DoctorProfileResponse> getAllDoctors();
    List<DoctorProfileResponse> findBySpecialization(String specialization);
    DoctorProfileResponse updateProfile(DoctorProfileRequest request);
}
