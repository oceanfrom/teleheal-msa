package thmsa.userservice.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PatientHistoryResponse(
        UUID appointmentId,
        LocalDateTime appointmentDate,
        String status,
        String doctorName,
        String patientName,
        List<MedicalRecordResponse> medicalRecord
) {}
