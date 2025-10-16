package thmsa.appointmentservice.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventProcessor {
    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${appointment.topic.name}")
    private String APPOINTMENT_TOPIC_NAME;
    @Value("${appointment.topic.dlq.name}")
    private String APPOINTMENT_TOPIC_DLQ_NAME;
    @Value("${outbox.max-retries}")
    private int MAX_RETRIES;

    @Scheduled(fixedDelayString = "${outbox.poll-interval-ms}")
    @Transactional
    public void processOutboxEvents() {
        var events = outboxEventRepository.findAllBySentFalseOrderByCreatedAtAsc();
        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(APPOINTMENT_TOPIC_NAME, event.getPayload()).get();
                event.setSent(true);
                event.setSentAt(LocalDateTime.now());
                outboxEventRepository.save(event);
                log.info("Published outbox event {}", event.getId());
            } catch (Exception e) {
                handleFailure(event, e);
                log.error("Failed to publish outbox event {}: {}", event.getId(), e.getMessage());
            }
        }
    }

    private void handleFailure(OutboxEvent event, Exception e) {
        event.setRetryCount(event.getRetryCount() + 1);
        event.setLastAttemptAt(LocalDateTime.now());

        if (event.getRetryCount() >= MAX_RETRIES) {
            event.setFailed(true);
            log.error("Outbox event moved to DLQ after {} retries: {}", MAX_RETRIES, event.getId(), e);
            sendToDeadLetterQueue(event);
        } else {
            log.warn("Retry {}/{} for event {} failed", event.getRetryCount(), MAX_RETRIES, event.getId());
        }
    }

    private void sendToDeadLetterQueue(OutboxEvent event) {
        try {
            kafkaTemplate.send(APPOINTMENT_TOPIC_DLQ_NAME, event.getPayload());
        } catch (Exception ex) {
            log.error("Failed to send event {} to DLQ", event.getId(), ex);
        }
    }
}
