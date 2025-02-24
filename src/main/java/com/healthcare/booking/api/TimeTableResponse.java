package com.healthcare.booking.api;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeTableResponse {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String patientName;
    private String doctorName;
    private LocalDateTime appointmentTime;
    private String description;
    private String statusLabel;
}
