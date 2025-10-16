package thmsa.appointmentservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "weekly_slots")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeeklySlot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID doctorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private DayOfWeek dayOfWeek;


    private LocalTime startTime;
    private LocalTime endTime;

    private boolean isActive = true;

    private LocalDate dateOverride;

    @Version
    private Long version;
}
