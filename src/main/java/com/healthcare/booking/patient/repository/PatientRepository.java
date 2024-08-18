package com.healthcare.booking.patient.repository;

import com.healthcare.booking.patient.model.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PatientRepository extends JpaRepository<PatientModel, Long>, JpaSpecificationExecutor<PatientModel> {
}
