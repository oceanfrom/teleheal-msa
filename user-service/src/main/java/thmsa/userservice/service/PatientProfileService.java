package thmsa.userservice.service;

import thmsa.userservice.domain.dto.PatientHistoryResponse;
import thmsa.userservice.domain.dto.PatientProfileRequest;
import thmsa.userservice.domain.dto.PatientProfileResponse;
import thmsa.userservice.domain.model.PatientProfile;

import java.util.List;
import java.util.UUID;

public interface PatientProfileService {
    PatientProfileResponse createPatientProfile(PatientProfileRequest patientProfileRequest);
    PatientProfileResponse updatePatient(PatientProfileRequest request);
    PatientProfileResponse getPatientById(UUID patientId);
    List<PatientProfileResponse> getAllPatients();
    List<PatientHistoryResponse> getPatientHistory(UUID patientId);
}
