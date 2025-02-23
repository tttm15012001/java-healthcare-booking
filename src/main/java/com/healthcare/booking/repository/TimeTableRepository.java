package com.healthcare.booking.repository;

import com.healthcare.booking.model.TimeTableModel;
import com.healthcare.booking.provider.StatusCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTableModel, Long> {

    Page<TimeTableModel> findByPatientId(Long patientId, Pageable pageable);

    List<TimeTableModel> findByAppointmentTimeBetweenAndStatusNot(LocalDateTime from, LocalDateTime to, Integer status);

    long countByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new com.healthcare.booking.provider.StatusCount(t.status, COUNT(t)) " +
            "FROM TimeTableModel t " +
            "WHERE t.appointmentTime BETWEEN :start AND :end " +
            "GROUP BY t.status")
    List<StatusCount> countStatusesBetween(LocalDateTime start, LocalDateTime end);

    Page<TimeTableModel> findByAppointmentTimeBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);

    @Query("SELECT ttm " +
            "FROM TimeTableModel ttm " +
            "WHERE ttm.appointmentTime BETWEEN :from AND :to ")
    List<TimeTableModel> findByAppointmentTimeBetweenWithoutPageable(LocalDateTime from, LocalDateTime to);
}
