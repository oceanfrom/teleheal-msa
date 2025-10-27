package thmsa.userservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AppointmentServiceClient {

    private final RestTemplate restTemplate;
    @Value("${appointment.service.url}")
    private String APPOINTMENT_SERVICE_URL;

    public List<AppointmentResponse> getAppointmentsByPatientId(UUID patientId) {
        var appointmentResponses = restTemplate.getForObject(
                APPOINTMENT_SERVICE_URL + patientId,
                AppointmentResponse[].class
        );

        if (appointmentResponses == null) return List.of();
        return Arrays.asList(appointmentResponses);
    }

    public List<AppointmentResponse> getUpcomingAppointments(UUID patientId) {
        var appointmentResponses = restTemplate.getForObject(
                APPOINTMENT_SERVICE_URL + patientId,
                AppointmentResponse[].class
        );

        if (appointmentResponses == null) return List.of();
        return Arrays.asList(appointmentResponses);
    }
}
