package thmsa.authservice.mapper;

import org.springframework.stereotype.Component;
import thmsa.authservice.domain.dto.*;
import thmsa.authservice.domain.model.*;

@Component
public class AuthMapper {
    public User toUserPatientProfile(PatientRegistrationRequest dto) {
        var user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(null)
                .roles(null)
                .dateOfBirth(dto.dateOfBirth())
                .phoneNumber(dto.phoneNumber())
                .build();

        var patientProfile = PatientProfile.builder()
                .user(user)
                .emergencyContact(dto.emergencyContact())
                .build();

        user.setPatientProfile(patientProfile);

        return user;
    }

    public User toUserDoctorProfile(DoctorRegistrationRequest dto) {
        var user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(null)
                .roles(null)
                .dateOfBirth(dto.dateOfBirth())
                .phoneNumber(dto.phoneNumber())
                .build();

        var doctorProfile = DoctorProfile.builder()
                .user(user)
                .specialization(dto.specialization())
                .licenseNumber(dto.licenseNumber())
                .isVerifiedDoctor(false)
                .build();

        user.setDoctorProfile(doctorProfile);

        return user;
    }

    public PatientProfileRegistrationResponse toPatientResponse(User user) {
        return new PatientProfileRegistrationResponse(
                user.getId(),
                user.getPatientProfile().getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().toString()
        );
    }


    public DoctorProfileRegistrationResponse toDoctorResponse(User user) {
        return new DoctorProfileRegistrationResponse(
                user.getId(),
                user.getDoctorProfile().getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().toString(),
                user.getDoctorProfile().isVerifiedDoctor()
        );
    }
}
