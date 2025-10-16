package thmsa.userservice.domain.dto;

import java.util.UUID;

public record DoctorProfileResponse(
        UUID id,
        UUID userId,
        String name,
        String email,
        String specialization,
        String licenseNumber,
        boolean isVerifiedDoctor
) { }