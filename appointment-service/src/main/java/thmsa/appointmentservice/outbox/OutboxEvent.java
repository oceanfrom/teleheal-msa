package thmsa.appointmentservice.outbox;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String aggregateType;
    private UUID aggregateId;
    private String eventType;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String payload;

    private boolean sent = false;
    private boolean failed;
    private int retryCount;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastAttemptAt;
    private LocalDateTime sentAt;
}
