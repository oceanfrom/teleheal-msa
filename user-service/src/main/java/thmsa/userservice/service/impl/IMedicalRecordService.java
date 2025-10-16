package thmsa.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thmsa.userservice.domain.dto.MedicalHistoryEntryRequest;
import thmsa.userservice.domain.dto.MedicalRecordRequest;
import thmsa.userservice.domain.dto.MedicalRecordResponse;
import thmsa.userservice.mapper.MedicalRecordMapper;
import thmsa.userservice.repository.MedicalHistoryEntryRepository;
import thmsa.userservice.repository.MedicalRecordRepository;
import thmsa.userservice.service.MedicalRecordService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IMedicalRecordService implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalHistoryEntryRepository medicalHistoryEntryRepository;
    private final MedicalRecordMapper medicalRecordMapper;


    @Override
    public MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request) {
        return null;
    }

    @Override
    public MedicalRecordResponse getMedicalRecordById(UUID id) {
        return null;
    }

    @Override
    public MedicalRecordResponse addHistoryEntry(UUID recordId, MedicalHistoryEntryRequest request, UUID doctorId) {
        return null;
    }

    @Override
    public MedicalRecordResponse updateHistoryEntry(UUID recordId, UUID entryId, MedicalHistoryEntryRequest request, UUID doctorId) {
        return null;
    }

    @Override
    public void deleteHistoryEntry(UUID recordId, UUID entryId, UUID doctorId) {

    }
}
