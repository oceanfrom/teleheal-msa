package thmsa.appointmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thmsa.appointmentservice.domain.enums.AppointmentStatus;
import thmsa.appointmentservice.domain.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    boolean existsByDoctorIdAndAppointmentDate(UUID doctorId, LocalDateTime appointmentDate);
    boolean existsByDoctorIdAndAppointmentDateAfter(UUID doctorId, LocalDateTime appointmentDate);
    List<Appointment> findAllByStatusAndCreatedAtBefore(AppointmentStatus status, LocalDateTime dateTime);

}
