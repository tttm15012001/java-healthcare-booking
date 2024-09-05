package com.healthcare.booking.patient.repository;

import com.healthcare.booking.patient.model.PatientModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface PatientRepository extends JpaRepository<PatientModel, Long>, JpaSpecificationExecutor<PatientModel> {
    /* Set limit for collection */
    @Query("SELECT p FROM PatientModel p ORDER BY p.createdAt DESC")
    List<PatientModel> select5NewestPatients(Pageable pageable);
}
