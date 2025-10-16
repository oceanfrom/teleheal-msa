package thmsa.authservice.service;

import thmsa.authservice.domain.dto.DoctorProfileRegistrationResponse;
import thmsa.authservice.domain.dto.DoctorRegistrationRequest;
import thmsa.authservice.domain.dto.PatientRegistrationRequest;
import thmsa.authservice.domain.dto.PatientProfileRegistrationResponse;

public interface AuthService {
    PatientProfileRegistrationResponse registerPatient(PatientRegistrationRequest patientRegistrationRequest);
    DoctorProfileRegistrationResponse registerDoctor(DoctorRegistrationRequest doctorRegistrationRequest);
}
