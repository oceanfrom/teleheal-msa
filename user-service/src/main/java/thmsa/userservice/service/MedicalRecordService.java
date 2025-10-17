package thmsa.userservice.service;

import thmsa.userservice.domain.dto.*;

import java.util.UUID;

public interface MedicalRecordService {

    MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request);

    MedicalRecordResponse getMedicalRecordById(UUID id);

    MedicalRecordResponse addHistoryEntry(MedicalHistoryEntryRequest medicalHistoryEntryRequest);

    MedicalRecordResponse updateHistoryEntry(UUID entryId, MedicalHistoryEntryRequest medicalHistoryEntryRequest);

    void deleteHistoryEntry(MedicalHistoryEntryDeleteRequest medicalHistoryEntryDeleteRequest);
}
