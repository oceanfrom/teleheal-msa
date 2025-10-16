package thmsa.appointmentservice.domain.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WeeklySlotRequest(
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {}