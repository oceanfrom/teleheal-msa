    package thmsa.notificationservice.event;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.kafka.annotation.KafkaListener;
    import org.springframework.stereotype.Service;

    @Service
    @Slf4j
    @RequiredArgsConstructor
    public class KafkaEventListener {
        private final ObjectMapper objectMapper;

        @KafkaListener(topics = "${appointment.topic.name}", groupId = "appointmentGroup")
        public void consumeEvent(String message) {
            try {
                var event = objectMapper.readValue(message, AppointmentEvent.class);
                handleEvent(event);
            } catch (Exception e) {
                log.error("Error during processing notification {}", e.getMessage(), e);
            }
        }

        private void handleEvent(AppointmentEvent event) {
            if (event == null || event.eventType() == null) {
                log.warn("Received event with null type: {}", event);
                return;
            }

            switch (event.eventType()) {
                case "CREATED" -> handleCreated(event);
                case "CONFIRMED" -> handleConfirmed(event);
                case "CANCELLED" -> handleCancelled(event);
                case "RESCHEDULED" -> handleRescheduled(event);
                default -> log.warn("Unknown event type: {}", event.eventType());
            }
        }

        private void handleCreated(AppointmentEvent event) {
            log.info("Appointment CREATED: {}", event.appointmentId());
        }

        private void handleConfirmed(AppointmentEvent event) {
            log.info("Appointment CONFIRMED: {}", event.appointmentId());
        }

        private void handleCancelled(AppointmentEvent event) {
            log.info("Appointment CANCELLED: {}", event.appointmentId());
        }

        private void handleRescheduled(AppointmentEvent event) {
            log.info("Appointment RESCHEDULED: {}", event.appointmentId());
        }
    }
