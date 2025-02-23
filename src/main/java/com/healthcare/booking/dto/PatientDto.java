package com.healthcare.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {
    private String fullName;
    private String patientCode;
    private String email;
    private String createdAt;
    private String phoneNumber;
    private String address;
    private String gender;
    private String birthday;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpecialityDto {
        private String code;
        private String label;
    }
}
