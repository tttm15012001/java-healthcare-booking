package com.healthcare.booking.doctor.repository;

import com.healthcare.booking.doctor.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
}
