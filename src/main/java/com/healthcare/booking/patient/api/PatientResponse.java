package com.healthcare.booking.patient.api;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatientResponse {
    private long id;
    private String fullName;
    private String statusLabel;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String birthDay;
    private LocalDateTime createdAt;
}
