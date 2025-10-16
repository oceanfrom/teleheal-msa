package thmsa.userservice.mapper;

import org.springframework.stereotype.Component;
import thmsa.userservice.domain.dto.UserRequest;
import thmsa.userservice.domain.dto.UserResponse;
import thmsa.userservice.domain.enums.UserRole;
import thmsa.userservice.domain.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        if (user == null) return null;
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getRoles() != null
                        ? user.getRoles().stream().map(UserRole::name).collect(Collectors.toList())
                        : null
        );
    }

    public User toEntity(UserRequest request) {
        if (request == null) return null;
        return User.builder()
                .name(request.fullName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .dateOfBirth(request.dateOfBirth())
                .roles(request.roles() != null
                        ? request.roles().stream().map(UserRole::valueOf).collect(Collectors.toSet())
                        : Set.of())
                .build();
    }
}
