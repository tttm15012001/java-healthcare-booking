package com.healthcare.booking.doctor.api;

import com.healthcare.booking.doctor.model.DoctorModel;
import com.healthcare.booking.doctor.model.Speciality;
import com.healthcare.booking.doctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorApi {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/getAllSpecialities")
    public List<Speciality> getAllSpecialities() {
        return doctorService.getAllSpecialities();
    }

    @GetMapping("/getAll")
    public List<DoctorModel> getAllDoctors() {
        return doctorService.getListDoctors();
    }

    @GetMapping("/{speciality}")
    public List<DoctorModel> getDoctorsBySpeciality(@PathVariable String speciality) {
        return doctorService.getAllDoctorBySpeciality(speciality);
    }
}
