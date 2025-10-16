package thmsa.appointmentservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thmsa.appointmentservice.domain.dto.*;
import thmsa.appointmentservice.service.WeeklySlotService;

@RestController
@RequestMapping("/api/v1/weekly-slots")
@RequiredArgsConstructor
public class WeeklySlotController {

    private final WeeklySlotService weeklySlotService;

    @PostMapping("/daily")
    public ResponseEntity<?> createDailySlots(@RequestBody DailySlotCreateRequest request) {
        return ResponseEntity.ok(weeklySlotService.createDailySlots(request));
    }

    @PostMapping("/weekly")
    public ResponseEntity<?> createWeeklySlots(@RequestBody WeeklySlotsCreateRequest request) {
        return ResponseEntity.ok(weeklySlotService.createWeeklySlots(request));
    }

    @PutMapping("/range")
    public ResponseEntity<?> updateSlotsByRange(@RequestBody SlotRangeUpdateRequest request) {
        return ResponseEntity.ok(weeklySlotService.updateSlotsByRange(request));
    }
}
