package thmsa.appointmentservice.repository;

import org.springframework.stereotype.Repository;
import thmsa.appointmentservice.domain.model.WeeklySlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface WeeklySlotRepository extends JpaRepository<WeeklySlot, UUID> {
    List<WeeklySlot> findByDoctorId(UUID doctorId);

    List<WeeklySlot> findByDoctorIdAndDayOfWeek(UUID doctorId, DayOfWeek dayOfWeek);

    List<WeeklySlot> findByDoctorIdAndDateOverride(UUID doctorId, LocalDate dateOverride);

    List<WeeklySlot> findByDoctorIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
            UUID doctorId, DayOfWeek dayOfWeek, LocalTime endTime, LocalTime startTime
    );

    List<WeeklySlot> findByDoctorIdAndDateOverrideAndStartTimeLessThanAndEndTimeGreaterThan(
            UUID doctorId, LocalDate dateOverride, LocalTime endTime, LocalTime startTime
    );

    List<WeeklySlot> findAllByDoctorIdAndDateOverrideBetween(UUID doctorId, LocalDate from, LocalDate to);

    List<WeeklySlot> findAllByDoctorIdAndDateOverrideAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
            UUID doctorId, LocalDate dateOverride, LocalTime startTime, LocalTime endTime
    );
}
