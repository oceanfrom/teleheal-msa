package thmsa.userservice.exception;

public class OutboxPublisherException extends RuntimeException {
    public OutboxPublisherException(String message) {
        super(message);
    }
}
