package com.healthcare.booking.service;

import com.healthcare.booking.dto.DoctorInfo;
import com.healthcare.booking.model.DoctorModel;
import com.healthcare.booking.model.Speciality;
import com.healthcare.booking.repository.DoctorRepository;
import com.healthcare.booking.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    public List<DoctorModel> getListDoctors()
    {
        return this.doctorRepository.findAll();
    }

    public DoctorModel getDoctorDetailById(Long id)
    {
        return this.doctorRepository.findById(id).orElse(null);
    }

    public List<DoctorModel> getAllDoctorBySpeciality(String speciality) {
        return this.doctorRepository.findBySpeciality_Code(speciality);
    }

    public List<Speciality> getAllSpecialities() {
        return this.specialityRepository.findAll();
    }

    public DoctorInfo buildDoctorInfoById(Long id) {
        DoctorModel doctor = getDoctorDetailById(id);
        return DoctorInfo.builder()
                .name(doctor.getName())
                .age(doctor.getAge())
                .speciality(doctor.getSpeciality())
                .build();
    }
}
