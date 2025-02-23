package com.healthcare.booking.api;

import lombok.Data;

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
