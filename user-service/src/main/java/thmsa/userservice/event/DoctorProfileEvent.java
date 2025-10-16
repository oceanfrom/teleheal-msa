package thmsa.userservice.event;


import lombok.Builder;

import java.util.UUID;

@Builder
public record DoctorProfileEvent(
        UUID doctorId,
        UUID userId,
        String name,
        String email,
        String specialization,
        String licenseNumber,
        boolean verified,
        EventType eventType
) {
}