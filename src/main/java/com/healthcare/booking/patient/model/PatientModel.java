package com.healthcare.booking.patient.model;

import com.healthcare.booking.patient.provider.Status;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Entity
@Table(name = "patient_entity")
@Data
public class PatientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    @Column(name = "patient_code", unique = true, nullable = false)
    private String patientCode;
    private String email;
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private int status = 0;
    @Column(name = "created_at", updatable = false)
    private String createdAt;
    private String phoneNumber;
    private String address;
    private String gender;
    private String birthday;

    @Transient
    private String statusLabel;

    /* Set data for column created_at right after the entity is created */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        createdAt = now.format(formatter);

        generatePatientCode();
    }

    @PostPersist
    private void updatePatientCodeWithId() {
        String oldCode = this.patientCode;
        String randomNumber = oldCode.substring(oldCode.length() - 5);
        randomNumber = String.valueOf(Integer.parseInt(randomNumber) + id);
        String newCode = oldCode.substring(0, oldCode.length() - 5) + randomNumber;
        this.patientCode = newCode;
    }

    private void generatePatientCode() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String dateTimeString = now.format(formatter);
        Random random = new Random();
        int randomNumber = random.nextInt(90000) + 10000;
        this.patientCode = dateTimeString + randomNumber;
    }

    @PostLoad
    private void loadStatusLabel() {
        this.statusLabel = Status.getLabelByCode(this.status);
    }
}
