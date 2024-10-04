package com.healthcare.booking.timetable.provider;

import lombok.Data;

@Data
public class TimeTableDTO {
    private Long id;
    private String appointmentTime;
    private String patientName;
    private String doctorName;
    private int status;
    private String statusLabel;
}

