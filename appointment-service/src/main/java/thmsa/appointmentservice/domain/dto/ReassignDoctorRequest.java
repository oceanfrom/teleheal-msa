package thmsa.appointmentservice.domain.dto;

import java.util.UUID;

public record ReassignDoctorRequest(
        UUID appointmentId,
        UUID newDoctorId
) {}