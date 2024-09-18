package com.healthcare.booking.timetable.model;

import com.healthcare.booking.patient.model.PatientModel;
import com.healthcare.booking.doctor.model.DoctorModel;
import com.healthcare.booking.timetable.provider.Status;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "timetable_management")
@Data
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

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private int status = 0;

    @Transient
    private String statusLabel;
    @PostLoad
    private void loadStatusLabel() {
        this.statusLabel = Status.getLabelByCode(this.status);
    }
}
