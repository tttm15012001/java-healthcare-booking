package com.healthcare.booking.doctor.service;

import com.healthcare.booking.doctor.model.DoctorModel;
import com.healthcare.booking.doctor.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public List<DoctorModel> getListDoctors()
    {
        return this.doctorRepository.findAll();
    }

    public DoctorModel getDoctorDetailById(Long id)
    {
        return this.doctorRepository.findById(id).orElse(null);
    }
}
