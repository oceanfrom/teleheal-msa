package thmsa.authservice.domain.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record DoctorRegistrationRequest(
        @NotBlank(message = "Name is required")
        String name,
        @Email(message = "Invalid email format") @NotBlank(message = "Email is required")
        String email,
        @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters")
        String password,
        @NotBlank(message = "Phone number is required")
        String phoneNumber,
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,
        @NotBlank(message = "Specialization is required")
        String specialization,
        @NotBlank(message = "License number is required")
        String licenseNumber
) {
}
