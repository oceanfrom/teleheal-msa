package thmsa.userservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AppointmentServiceClient {

    private final RestTemplate restTemplate;
    private final String APPOINTMENT_SERVICE_URL = "http://localhost:8113/api/v1/appointments/patient/";

    public List<AppointmentResponse> getAppointmentsByPatientId(UUID patientId) {
        AppointmentResponse[] response = restTemplate.getForObject(
                APPOINTMENT_SERVICE_URL + patientId,
                AppointmentResponse[].class
        );

        if (response == null) return List.of();
        return Arrays.asList(response);
    }
}
