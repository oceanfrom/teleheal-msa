package thmsa.appointmentservice.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import thmsa.appointmentservice.event.EventType;
import thmsa.appointmentservice.exception.OutboxPublisherException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventPublisher {
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public void publish(Object event, String aggregateType, UUID aggregateId, EventType eventType) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .aggregateType(aggregateType)
                    .aggregateId(aggregateId)
                    .eventType(eventType.toString())
                    .payload(payload)
                    .sent(false)
                    .build();
            outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new OutboxPublisherException("Failed to serialize event");
        }
    }
}
