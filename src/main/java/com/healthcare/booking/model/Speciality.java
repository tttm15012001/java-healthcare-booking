package com.healthcare.booking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "speciality", uniqueConstraints = {@UniqueConstraint(columnNames = "code")})
public class Speciality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;
    private String label;
}
