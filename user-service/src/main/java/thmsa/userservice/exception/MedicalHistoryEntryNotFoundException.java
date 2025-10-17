package thmsa.userservice.exception;

public class MedicalHistoryEntryNotFoundException extends RuntimeException {
    public MedicalHistoryEntryNotFoundException(String message) {
        super(message);
    }
}
