package thmsa.userservice.domain.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;
@Builder
public record MedicalHistoryEntryResponse(
        LocalDate date,
        UUID medicalRecordId,
        UUID appointmentId,
        String diagnosis,
        String notes
) {}