package thmsa.appointmentservice.domain.model;

import jakarta.persistence.*;
import lombok.*;
import thmsa.appointmentservice.domain.enums.AppointmentStatus;
import thmsa.appointmentservice.domain.enums.PerformedByType;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "appointment_history")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus action;

    @Enumerated(EnumType.STRING)
    private PerformedByType performedBy;

    private LocalDateTime timestamp;
    private LocalDateTime oldDate;
    private LocalDateTime newDate;

    private String reason;
}
