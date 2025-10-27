package thmsa.notificationservice.event.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thmsa.notificationservice.event.AppointmentEvent;
import thmsa.notificationservice.event.handler.AppointmentEventHandler;

@Component
@Slf4j
public class AppointmentUpcomingHandler implements AppointmentEventHandler {

    @Value("${appointment.event.type.upcoming}")
    private String eventType;

    @Override
    public void handle(AppointmentEvent appointmentEvent) {
        log.info("Appointment upcoming, {}", appointmentEvent.appointmentId());
    }

    @Override
    public boolean handleEventType(AppointmentEvent appointmentEvent) {
        return eventType.equals(appointmentEvent.eventType());
    }
}
