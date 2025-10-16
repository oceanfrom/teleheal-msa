package thmsa.appointmentservice.service;

import thmsa.appointmentservice.domain.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WeeklySlotService {
    boolean isSlotAvailable(UUID doctorId, LocalDateTime appointmentDate);
    boolean isDoctorAvailableAt(UUID doctorId, LocalDateTime dateTime);
    List<WeeklySlotResponse> createDailySlots(DailySlotCreateRequest dailySlotCreateRequest);
    List<WeeklySlotResponse> createWeeklySlots(WeeklySlotsCreateRequest weeklySlotsCreateRequest);
    List<WeeklySlotResponse> updateSlotsByRange(SlotRangeUpdateRequest request);


}
