package thmsa.userservice.domain.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MedicalHistoryEntryDeleteRequest(
        @NotNull
        UUID recordId,
        @NotNull
        UUID entryId,
        @NotNull
        UUID doctorId
) {
}
