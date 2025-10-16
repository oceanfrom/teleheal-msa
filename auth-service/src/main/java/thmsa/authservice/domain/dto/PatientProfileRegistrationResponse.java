package thmsa.authservice.domain.dto;

import java.util.UUID;

public record PatientProfileRegistrationResponse(
        UUID userId,
        UUID profileId,
        String name,
        String email,
        String role
) {
}
