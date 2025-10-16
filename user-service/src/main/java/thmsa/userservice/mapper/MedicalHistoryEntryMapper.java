package thmsa.userservice.mapper;

import org.springframework.stereotype.Component;

@Component
public class MedicalHistoryEntryMapper {

//    public MedicalHistoryEntry toEntity(MedicalHistoryEntryRequest request, MedicalRecord record, Appointment appointment) {
//        return MedicalHistoryEntry.builder()
//                .medicalRecord(record)
//                .doctorId(request.doctorId())
//                .diagnosis(request.diagnosis())
//                .notes(request.notes())
//                .date(request.date())
//                .appointment(appointment)
//                .build();
//    }
//
//    public MedicalHistoryEntryResponse toResponse(MedicalHistoryEntry entry) {
//        return new MedicalHistoryEntryResponse(
//                entry.getId(),
//                entry.getMedicalRecord().getId(),
//                entry.getDoctorId(),
//                entry.getDoctorName(),
//                entry.getDiagnosis(),
//                entry.getNotes(),
//                entry.getDate(),
//                entry.getAppointment().getId()
//        );
//    }
}
