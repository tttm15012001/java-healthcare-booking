package com.healthcare.booking.model;

import com.healthcare.booking.provider.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "timetable_management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientModel patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorModel doctor;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @Column(name = "description")
    private String description;

    @Column(name = "appointment_type", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private int appointmentType;

    @Column(name = "patient_information", nullable = false)
    private String patientInformation;

    @Column(name = "reference_information", nullable = false)
    private String referenceInformation;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private int status = 0;

    @Transient
    private String statusLabel;
    @PostLoad
    private void loadStatusLabel() {
        this.statusLabel = Status.getLabelByCode(this.status);
    }
}
