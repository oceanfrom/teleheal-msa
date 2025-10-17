package thmsa.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thmsa.userservice.domain.dto.MedicalHistoryEntryDeleteRequest;
import thmsa.userservice.domain.dto.MedicalHistoryEntryRequest;
import thmsa.userservice.domain.dto.MedicalRecordRequest;
import thmsa.userservice.domain.dto.MedicalRecordResponse;
import thmsa.userservice.exception.*;
import thmsa.userservice.mapper.MedicalHistoryEntryMapper;
import thmsa.userservice.mapper.MedicalRecordMapper;
import thmsa.userservice.repository.DoctorProfileRepository;
import thmsa.userservice.repository.MedicalHistoryEntryRepository;
import thmsa.userservice.repository.MedicalRecordRepository;
import thmsa.userservice.repository.PatientProfileRepository;
import thmsa.userservice.service.MedicalRecordService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IMedicalRecordService implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalHistoryEntryRepository medicalHistoryEntryRepository;
    private final MedicalHistoryEntryMapper medicalHistoryEntryMapper;
    private final PatientProfileRepository patientProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final MedicalRecordMapper medicalRecordMapper;


    @Transactional
    @Override
    public MedicalRecordResponse createMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        var patientProfile = patientProfileRepository.findById(medicalRecordRequest.patientId())
                .orElseThrow(() -> new PatientProfileNotFoundException("Patient with id " + medicalRecordRequest.patientId() + " not found"));
        var doctorProfile = doctorProfileRepository.findById(medicalRecordRequest.doctorId())
                .orElseThrow(() -> new DoctorProfileNotFoundException("Doctor with id " + medicalRecordRequest.doctorId() + " not found"));

        var medicalRecord = medicalRecordMapper.toEntity(patientProfile, doctorProfile);
        medicalRecordRepository.save(medicalRecord);
        return medicalRecordMapper.toResponse(medicalRecord);
    }

    @Transactional(readOnly = true)
    @Override
    public MedicalRecordResponse getMedicalRecordById(UUID id) {
        var medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record with id" + id +" not found"));
        return medicalRecordMapper.toResponse(medicalRecord);
    }

    @Transactional
    @Override
    public MedicalRecordResponse addHistoryEntry(MedicalHistoryEntryRequest medicalHistoryEntryRequest) {
        var medicalRecord = medicalRecordRepository.findById(medicalHistoryEntryRequest.medicalRecordId())
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record with id" + medicalHistoryEntryRequest.medicalRecordId() + " not found"));

        if(!(medicalRecord.getDoctor().getId().equals(medicalHistoryEntryRequest.doctorId())))
            throw new UnauthorizedDoctorException("Only the assigned doctor can add history");

        var medicalRecordHistoryEntry = medicalHistoryEntryMapper.toEntity(medicalHistoryEntryRequest, medicalRecord);
        medicalHistoryEntryRepository.save(medicalRecordHistoryEntry);
        return medicalRecordMapper.toResponse(medicalRecord);

    }

    @Transactional
    @Override
    public MedicalRecordResponse updateHistoryEntry(UUID entryId, MedicalHistoryEntryRequest medicalHistoryEntryRequest) {
        var medicalRecord = medicalRecordRepository.findById(medicalHistoryEntryRequest.medicalRecordId())
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record with id" + medicalHistoryEntryRequest.medicalRecordId() + " not found"));

        if(!(medicalRecord.getDoctor().getId().equals(medicalHistoryEntryRequest.doctorId())))
            throw new UnauthorizedDoctorException("Only the assigned doctor can change history");

        var medicalHistoryEntry = medicalHistoryEntryRepository.findById(entryId)
                        .orElseThrow(() -> new MedicalHistoryEntryNotFoundException("Medical history entry with id" + entryId + " not found"));

        medicalHistoryEntry.setDate(medicalHistoryEntryRequest.date());
        medicalHistoryEntry.setDiagnosis(medicalHistoryEntryRequest.diagnosis());
        medicalHistoryEntry.setNotes(medicalHistoryEntryRequest.notes());

        medicalRecordRepository.save(medicalRecord);
        return medicalRecordMapper.toResponse(medicalRecord);

    }

    @Transactional
    @Override
    public void deleteHistoryEntry(MedicalHistoryEntryDeleteRequest medicalHistoryEntryDeleteRequest) {
        var medicalRecord = medicalRecordRepository.findById(medicalHistoryEntryDeleteRequest.recordId())
                .orElseThrow(() -> new RuntimeException("Medical record not found: " + medicalHistoryEntryDeleteRequest.recordId()));

        if (!medicalRecord.getDoctor().getId().equals(medicalHistoryEntryDeleteRequest.doctorId()))
            throw new RuntimeException("Only the assigned doctor can delete history");

        medicalRecord.getHistory().removeIf(e -> e.getId().equals(medicalHistoryEntryDeleteRequest.entryId()));
        medicalRecordRepository.save(medicalRecord);
    }
}
