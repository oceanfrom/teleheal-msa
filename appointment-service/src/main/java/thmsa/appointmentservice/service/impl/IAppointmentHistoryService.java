package thmsa.appointmentservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import thmsa.appointmentservice.domain.dto.AppointmentHistoryResponse;
import thmsa.appointmentservice.domain.enums.AppointmentStatus;
import thmsa.appointmentservice.domain.enums.PerformedByType;
import thmsa.appointmentservice.domain.model.Appointment;
import thmsa.appointmentservice.domain.model.AppointmentHistory;
import thmsa.appointmentservice.mapper.AppointmentHistoryMapper;
import thmsa.appointmentservice.repository.AppointmentHistoryRepository;
import thmsa.appointmentservice.service.AppointmentHistoryService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class IAppointmentHistoryService implements AppointmentHistoryService {
    private final AppointmentHistoryRepository appointmentHistoryRepository;
    private final AppointmentHistoryMapper appointmentHistoryMapper;

    @Override
    public AppointmentHistoryResponse record(Appointment appointment,
                                             AppointmentStatus appointmentStatus,
                                             PerformedByType performedByType,
                                             String reason,
                                             LocalDateTime oldDate,
                                             LocalDateTime newDate) {
        var appointmentHistory = AppointmentHistory.builder()
                .appointment(appointment)
                .action(appointmentStatus)
                .performedBy(performedByType)
                .reason(reason)
                .oldDate(oldDate)
                .newDate(newDate)
                .build();
        appointmentHistoryRepository.save(appointmentHistory);

        return appointmentHistoryMapper.toAppointmentHistoryResponse(appointmentHistory);
    }
}
