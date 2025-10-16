package thmsa.userservice.domain.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        List<String> roles
) {
}
