package thmsa.appointmentservice.exception;

public class OutboxPublisherException extends RuntimeException {
    public OutboxPublisherException(String message) {
        super(message);
    }
}
