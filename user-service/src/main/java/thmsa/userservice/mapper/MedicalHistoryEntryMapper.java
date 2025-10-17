package thmsa.userservice.mapper;

import org.springframework.stereotype.Component;
import thmsa.userservice.domain.dto.MedicalHistoryEntryRequest;
import thmsa.userservice.domain.dto.MedicalHistoryEntryResponse;
import thmsa.userservice.domain.model.MedicalHistoryEntry;
import thmsa.userservice.domain.model.MedicalRecord;

@Component
public class MedicalHistoryEntryMapper {
    public MedicalHistoryEntry toEntity(MedicalHistoryEntryRequest request, MedicalRecord record) {
        return MedicalHistoryEntry.builder()
                .medicalRecord(record)
                .diagnosis(request.diagnosis())
                .notes(request.notes())
                .date(request.date())
                .build();
    }

    public MedicalHistoryEntryResponse toResponse(MedicalHistoryEntry entry) {
        return new MedicalHistoryEntryResponse(
                entry.getId(),
                entry.getMedicalRecord().getDoctor().getId(),
                entry.getMedicalRecord().getDoctor().getUser().getName(),
                entry.getDiagnosis(),
                entry.getNotes(),
                entry.getDate()
        );
    }
}
