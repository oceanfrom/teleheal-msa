package thmsa.notificationservice.event.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import thmsa.notificationservice.event.AppointmentEvent;
import thmsa.notificationservice.event.handler.AppointmentEventHandler;

@Component
@Slf4j
public class AppointmentRescheduledHandler implements AppointmentEventHandler {

    @Value("${appointment.event.type.rescheduled}")
    private String eventType;

    @Override
    public void handle(AppointmentEvent appointmentEvent) {
        log.info("Appointment rescheduled, {}", appointmentEvent.appointmentId());
    }

    @Override
    public boolean handleEventType(AppointmentEvent appointmentEvent) {
        return eventType.equals(appointmentEvent.eventType());
    }
}
