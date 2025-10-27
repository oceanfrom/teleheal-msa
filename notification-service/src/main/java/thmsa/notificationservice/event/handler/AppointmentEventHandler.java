package thmsa.notificationservice.event.handler;

import thmsa.notificationservice.event.AppointmentEvent;

public interface AppointmentEventHandler {
    void handle(AppointmentEvent appointmentEvent);
    boolean handleEventType(AppointmentEvent appointmentEvent);
}
