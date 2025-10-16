package thmsa.userservice.domain.dto;

import java.time.LocalDate;
import java.util.List;

public record UserRequest(
        String name,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        List<String> roles
) {
}
