package thmsa.userservice.exception;

public class UnauthorizedDoctorException extends RuntimeException {
    public UnauthorizedDoctorException(String message) {
        super(message);
    }
}
