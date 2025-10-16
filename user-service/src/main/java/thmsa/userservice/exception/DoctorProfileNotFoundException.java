package thmsa.userservice.exception;

public class DoctorProfileNotFoundException extends RuntimeException {
    public DoctorProfileNotFoundException(String message) {
        super(message);
    }
}
