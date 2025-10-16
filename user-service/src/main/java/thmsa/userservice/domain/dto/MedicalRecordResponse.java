package thmsa.userservice.domain.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record MedicalRecordResponse(
        UUID id,
        UUID patientId,
        String patientName,
        UUID doctorId,
        String doctorName,
        LocalDateTime createdAt,
        List<MedicalHistoryEntryResponse> history
) {}