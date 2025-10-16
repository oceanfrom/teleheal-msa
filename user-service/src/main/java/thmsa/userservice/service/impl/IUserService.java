package thmsa.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thmsa.userservice.domain.dto.UserRequest;
import thmsa.userservice.domain.dto.UserResponse;
import thmsa.userservice.exception.UserNotFoundException;
import thmsa.userservice.mapper.UserMapper;
import thmsa.userservice.repository.UserRepository;
import thmsa.userservice.service.UserService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IUserService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public UserResponse getById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Transactional
    @Override
    public UserResponse update(UserRequest userRequest) {
        var user = userRepository.findByEmail(userRequest.email())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userRequest.email()));

        user.setName(userRequest.name());
        user.setPhoneNumber(userRequest.phoneNumber());
        user.setDateOfBirth(userRequest.dateOfBirth());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    @Override
    public void deleteById(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
       userRepository.delete(user);
    }
}
