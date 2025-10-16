package thmsa.authservice.domain.dto;

import java.util.UUID;

public record DoctorProfileRegistrationResponse(
        UUID userId,
        UUID profileId,
        String name,
        String email,
        String role,
        boolean isVerified
) {
}