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
public class DoctorInfo {
    private Long id;
    private String name;
    private String age;
    private Speciality speciality;
}
