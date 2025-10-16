package thmsa.userservice.service;

import thmsa.userservice.domain.dto.UserRequest;
import thmsa.userservice.domain.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAll();
    UserResponse getById(UUID id);
    UserResponse update(UserRequest userRequest);
    void deleteById(UUID id);
}
