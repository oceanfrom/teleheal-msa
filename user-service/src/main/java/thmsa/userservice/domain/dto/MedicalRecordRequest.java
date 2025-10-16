package thmsa.userservice.domain.dto;


import java.util.UUID;

public record MedicalRecordRequest(
        UUID patientId,
        UUID doctorId
) {}