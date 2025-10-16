package thmsa.appointmentservice.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record SlotRangeUpdateRequest(
        @NotNull UUID doctorId,
        @NotNull LocalDate fromDate,
        @NotNull LocalDate toDate,
        LocalTime fromTime,
        LocalTime toTime,
        Boolean isActive,
        Boolean delete
) {}
