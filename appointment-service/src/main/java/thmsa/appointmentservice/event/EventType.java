package thmsa.appointmentservice.event;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum EventType {
    CREATED,
    CONFIRMED,
    CANCELLED,
    RESCHEDULED
}
