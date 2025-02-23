package com.healthcare.booking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "doctor_management")
@Data
public class DoctorModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String age;
    private String gender;
    private String phone;
    private String address;
    private String email;
    private String degree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speciality_code", referencedColumnName = "code", nullable = false)
    private Speciality speciality;

    private String yearOfExperience;
}
