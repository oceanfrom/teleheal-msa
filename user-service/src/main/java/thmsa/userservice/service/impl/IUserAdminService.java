package thmsa.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import thmsa.userservice.repository.DoctorProfileRepository;
import thmsa.userservice.service.UserAdminService;

@Service
@RequiredArgsConstructor
@Slf4j
public class IUserAdminService implements UserAdminService {
    private final DoctorProfileRepository doctorProfileRepository;

}
