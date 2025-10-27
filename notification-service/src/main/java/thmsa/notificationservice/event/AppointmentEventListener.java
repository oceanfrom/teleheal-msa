    package thmsa.notificationservice.event;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.kafka.annotation.KafkaListener;
    import org.springframework.stereotype.Service;
    import thmsa.notificationservice.event.handler.AppointmentEventHandler;
    import thmsa.notificationservice.exception.AppointmentEventProcessingException;

    import java.util.List;

    @Service
    @Slf4j
    @RequiredArgsConstructor
    public class AppointmentEventListener {
        private final ObjectMapper objectMapper;
        private final List<AppointmentEventHandler> appointmentEventHandlers;

       @KafkaListener(topics = "${appointment.topic.name}", groupId = "${appointment.group-id.name}")
        public void consumeEvent(String message) {
            try {
                var event = objectMapper.convertValue(message, AppointmentEvent.class);
                handleEvent(event);
            } catch (AppointmentEventProcessingException ex) {
                throw new AppointmentEventProcessingException(ex.getMessage());
            }
       }

        private void handleEvent(AppointmentEvent appointmentEvent) {
            if (appointmentEvent == null || appointmentEvent.eventType() == null) return;

            appointmentEventHandlers.stream()
                    .filter(handler -> handler.handleEventType(appointmentEvent))
                    .findFirst()
                    .ifPresentOrElse(
                            handler -> handler.handle(appointmentEvent),
                            () -> log.info("No handler found for event type: {}", appointmentEvent.eventType())
                    );
        }
    }
