package thmsa.userservice.domain.dto;

import java.util.UUID;

public record PatientProfileRequest(
        UUID id,
        UUID userId,
        String emergencyContact
) {}