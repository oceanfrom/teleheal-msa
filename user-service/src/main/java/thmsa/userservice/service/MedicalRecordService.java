package thmsa.userservice.service;

import thmsa.userservice.domain.dto.*;

import java.util.UUID;

public interface MedicalRecordService {

    MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request);

    MedicalRecordResponse getMedicalRecordById(UUID id);

    MedicalRecordResponse addHistoryEntry(UUID recordId, MedicalHistoryEntryRequest request, UUID doctorId);

    MedicalRecordResponse updateHistoryEntry(UUID recordId, UUID entryId, MedicalHistoryEntryRequest request, UUID doctorId);

    void deleteHistoryEntry(UUID recordId, UUID entryId, UUID doctorId);
}
