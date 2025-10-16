package thmsa.userservice.domain.dto;

import java.util.UUID;

public record PatientProfileResponse(
        UUID id,
        UUID userId,
        String name,
        String email,
        String emergencyContact
) {
}
