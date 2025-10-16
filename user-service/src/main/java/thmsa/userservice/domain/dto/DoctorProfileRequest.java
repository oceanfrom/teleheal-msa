package thmsa.userservice.domain.dto;

import java.util.UUID;

public record DoctorProfileRequest(
        UUID id,
        UUID userId,
        String specialization,
        String licenseNumber
) {}