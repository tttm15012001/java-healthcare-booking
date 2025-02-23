package com.healthcare.booking.repository;

import com.healthcare.booking.model.DoctorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<DoctorModel, Long> {
    public List<DoctorModel> findBySpeciality_Code(String speciality);
}
