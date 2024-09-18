package com.healthcare.booking.doctor.repository;

import com.healthcare.booking.doctor.model.DoctorModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<DoctorModel, Long> {
}
