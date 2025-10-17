package thmsa.userservice.domain.dto;

import java.time.LocalDate;
import java.util.UUID;

public record MedicalHistoryEntryRequest(
        UUID medicalRecordId,
        UUID doctorId,
        String doctorName,
        String diagnosis,
        String notes,
        LocalDate date
) {}
