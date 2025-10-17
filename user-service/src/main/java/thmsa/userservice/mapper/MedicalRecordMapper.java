package thmsa.userservice.mapper;

import org.springframework.stereotype.Component;
import thmsa.userservice.domain.dto.MedicalHistoryEntryResponse;
import thmsa.userservice.domain.dto.MedicalRecordResponse;
import thmsa.userservice.domain.model.DoctorProfile;
import thmsa.userservice.domain.model.MedicalRecord;
import thmsa.userservice.domain.model.PatientProfile;

import java.util.stream.Collectors;


@Component
public class MedicalRecordMapper {

    public MedicalRecord toEntity(PatientProfile patientProfile, DoctorProfile doctorProfile) {
        return MedicalRecord.builder()
                .patient(patientProfile)
                .doctor(doctorProfile)
                .build();
    }

    public MedicalRecordResponse toResponse(MedicalRecord record) {
        return MedicalRecordResponse.builder()
                .id(record.getId())
                .patientId(record.getPatient().getId())
                .patientName(record.getPatient().getUser().getName())
                .doctorId(record.getDoctor().getId())
                .doctorName(record.getDoctor().getUser().getName())
                .createdAt(record.getCreatedAt().atStartOfDay())
                .history(record.getHistory().stream()
                        .map(h -> MedicalHistoryEntryResponse.builder()
                                .date(h.getDate())
                                .diagnosis(h.getDiagnosis())
                                .notes(h.getNotes())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
