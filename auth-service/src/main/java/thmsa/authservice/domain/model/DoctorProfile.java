package thmsa.authservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "doctors_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String specialization;
    private String licenseNumber;
    private boolean isVerifiedDoctor = false;

    private UUID appointmentServiceDoctorId; // connection via appointment-service
}
