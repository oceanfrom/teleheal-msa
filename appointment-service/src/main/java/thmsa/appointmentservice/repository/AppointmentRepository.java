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

    List<Appointment> findAppointmentsByPatientId(UUID patientId);

    List<Appointment> findAllByDoctorId(UUID doctorId);

    List<Appointment> findTop5ByPatientIdAndStatusInOrderByAppointmentDateAsc(UUID patientId, List<AppointmentStatus> statuses);

    List<Appointment> findAppointmentsWithinNextHours(LocalDateTime start, LocalDateTime end, List<AppointmentStatus> statuses);
}
