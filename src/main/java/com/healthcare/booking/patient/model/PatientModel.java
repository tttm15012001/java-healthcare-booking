package com.healthcare.booking.patient.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "patient_entity")
@Data
public class PatientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String birthDate;
}
