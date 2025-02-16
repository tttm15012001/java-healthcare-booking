package com.healthcare.booking.doctor.repository;

import com.healthcare.booking.doctor.model.DoctorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<DoctorModel, Long> {
    public List<DoctorModel> findBySpeciality_Code(String speciality);
}
