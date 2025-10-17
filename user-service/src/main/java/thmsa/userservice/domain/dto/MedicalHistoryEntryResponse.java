package thmsa.userservice.domain.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record MedicalHistoryEntryResponse(
        UUID medicalRecordId,
        UUID doctorId,
        String doctorName,
        String diagnosis,
        String notes,
        LocalDate date
) {
}