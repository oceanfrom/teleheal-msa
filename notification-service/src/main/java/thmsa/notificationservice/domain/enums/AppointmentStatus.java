package thmsa.notificationservice.domain.enums;

public enum AppointmentStatus {
    AVAILABLE,
    SCHEDULED,
    CONFIRMED,
    COMPLETED,
    CANCELLED_BY_PATIENT,
    CANCELLED_BY_DOCTOR,
    NO_SHOW,
    RESCHEDULED
}