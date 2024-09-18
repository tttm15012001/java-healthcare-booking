package com.healthcare.booking.timetable.repo;

import com.healthcare.booking.timetable.model.TimeTableModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTableModel, Long> {

    Page<TimeTableModel> findByPatientId(Long patientId, Pageable pageable);

    List<TimeTableModel> findByAppointmentTimeBetween(LocalDateTime from, LocalDateTime to);
}
