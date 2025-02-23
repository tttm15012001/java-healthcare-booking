package com.healthcare.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientInfo {
    private Long id;
    private String fullName;
    private String gender;
    private String birthday;
}
