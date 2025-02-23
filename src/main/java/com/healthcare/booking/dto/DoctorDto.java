package com.healthcare.booking.dto;

import com.healthcare.booking.model.Speciality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {
    private String name;
    private String age;
    private String gender;
    private String phone;
    private String address;
    private String email;
    private String degree;
    private Speciality speciality;
    private String yearOfExperience;
}
