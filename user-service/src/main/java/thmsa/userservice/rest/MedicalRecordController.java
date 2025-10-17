package thmsa.userservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thmsa.userservice.domain.dto.MedicalHistoryEntryDeleteRequest;
import thmsa.userservice.domain.dto.MedicalHistoryEntryRequest;
import thmsa.userservice.domain.dto.MedicalRecordRequest;
import thmsa.userservice.service.MedicalRecordService;

import java.util.UUID;

// refactor api later
@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicalRecordById(@PathVariable UUID id) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordById(id));
    }

    @PostMapping
    public ResponseEntity<?> createMedicalRecord(@RequestBody @Valid MedicalRecordRequest medicalRecordRequest) {
        return ResponseEntity.ok(medicalRecordService.createMedicalRecord(medicalRecordRequest));
    }

    @PostMapping("/history-entry/create")
    public ResponseEntity<?> addMedicalHistoryEntry(@RequestBody @Valid MedicalHistoryEntryRequest medicalHistoryEntryRequest) {
        return ResponseEntity.ok(medicalRecordService.addHistoryEntry(medicalHistoryEntryRequest));
    }

    @PutMapping("/{entryId}/update")
    public ResponseEntity<?> updateMedicalHistoryEntry(@PathVariable UUID entryId, @RequestBody @Valid MedicalHistoryEntryRequest medicalHistoryEntryRequest) {
        return ResponseEntity.ok(medicalRecordService.updateHistoryEntry(entryId, medicalHistoryEntryRequest));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMedicalHistoryEntry(@RequestBody @Valid MedicalHistoryEntryDeleteRequest medicalHistoryEntryDeleteRequest) {
        medicalRecordService.deleteHistoryEntry(medicalHistoryEntryDeleteRequest);
        return ResponseEntity.ok().build();
    }
}
