package thmsa.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thmsa.userservice.client.AppointmentResponse;
import thmsa.userservice.client.AppointmentServiceClient;
import thmsa.userservice.domain.dto.PatientHistoryResponse;
import thmsa.userservice.domain.dto.PatientProfileRequest;
import thmsa.userservice.domain.dto.PatientProfileResponse;
import thmsa.userservice.domain.model.PatientProfile;
import thmsa.userservice.exception.PatientProfileNotFoundException;
import thmsa.userservice.exception.UserNotFoundException;
import thmsa.userservice.mapper.MedicalRecordMapper;
import thmsa.userservice.mapper.PatientProfileMapper;
import thmsa.userservice.repository.MedicalRecordRepository;
import thmsa.userservice.repository.PatientProfileRepository;
import thmsa.userservice.repository.UserRepository;
import thmsa.userservice.service.PatientProfileService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IPatientProfileService implements PatientProfileService {
    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final PatientProfileMapper patientProfileMapper;
    private final AppointmentServiceClient appointmentServiceClient;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;

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

    @Transactional(readOnly = true)
    @Override
    public List<PatientHistoryResponse> getPatientHistory(UUID patientId) {
        var appointments = appointmentServiceClient.getAppointmentsByPatientId(patientId);

        return appointments.stream()
                .map(app -> {
                    var medicalRecord = medicalRecordRepository
                            .findByPatientIdAndDoctorId(patientId, app.doctorId());

                    var medicalRecordResponse = medicalRecord != null
                            ? medicalRecordMapper.toResponse(medicalRecord)
                            : null;

                    return new PatientHistoryResponse(
                            app.id(),
                            app.appointmentDate(),
                            app.appointmentStatus().name(),
                            app.doctorName(),
                            app.patientName(),
                            Collections.singletonList(medicalRecordResponse)
                    );
                })
                .toList();
    }

    @Override
    public List<AppointmentResponse> getPatientUpcomingAppointments(UUID patientId) {
        return appointmentServiceClient.getUpcomingAppointments(patientId);
    }
}
