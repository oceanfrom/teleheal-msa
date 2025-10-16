package thmsa.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thmsa.userservice.domain.model.MedicalHistoryEntry;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalHistoryEntryRepository extends JpaRepository<MedicalHistoryEntry, UUID> {
    List<MedicalHistoryEntry> findByMedicalRecordId(UUID medicalRecordId);

}