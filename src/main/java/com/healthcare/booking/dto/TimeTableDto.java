package com.healthcare.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableDto {
    private PatientInfo patientInfo;
    private DoctorInfo doctorInfo;
    private int status;
    private String statusLabel;
    private String patientInformation;
    private String referenceInformation;
    private int appointmentType;
    private LocalDateTime appointmentTime;
    private String description;
    private String formattedAppointmentTime;
}
