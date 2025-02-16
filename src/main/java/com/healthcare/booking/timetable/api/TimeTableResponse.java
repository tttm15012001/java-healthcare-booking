package com.healthcare.booking.timetable.api;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeTableResponse {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String patientName;
    private String doctorName;
    private String appointmentTime;
    private String description;
    private String statusLabel;
}
