package com.healthcare.booking.timetable.service;

import com.healthcare.booking.timetable.model.TimeTableModel;
import com.healthcare.booking.timetable.repo.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeTableService {
    @Autowired
    private TimeTableRepository timeTableRepository;

    public Page<TimeTableModel> getTimeTableByPatientId(Long patientId) {
        Pageable pageable = PageRequest.of(0, 5);
        return this.timeTableRepository.findByPatientId(patientId, pageable);
    }

    public List<TimeTableModel> getAppointmentsForDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        return this.timeTableRepository.findByAppointmentTimeBetween(start, end);
    }
}
