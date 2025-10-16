package thmsa.authservice.exception;

public class UserWithThisEmailAlreadyExists extends RuntimeException {
    public UserWithThisEmailAlreadyExists(String message) {
        super(message);
    }
}
