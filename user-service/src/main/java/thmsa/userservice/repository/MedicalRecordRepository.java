package thmsa.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thmsa.userservice.domain.model.MedicalRecord;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {
    List<MedicalRecord> findAllByPatientId(UUID patientId);
    List<MedicalRecord> findAllByDoctorId(UUID doctorId);
    MedicalRecord findByPatientIdAndDoctorId(UUID patientId, UUID doctorId);
}
