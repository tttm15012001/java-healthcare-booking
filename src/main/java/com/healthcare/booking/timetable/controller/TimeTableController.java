package com.healthcare.booking.timetable.controller;

import com.healthcare.booking.patient.provider.PatientDataProvider;
import com.healthcare.booking.timetable.model.TimeTableModel;
import com.healthcare.booking.timetable.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"admin/weekly-calendar", "admin/weekly-calendar/"})
public class TimeTableController {
    @Autowired
    private TimeTableService timeTableService;

    @GetMapping
    public String getWeeklyCalendar(Model model) {
        LocalDate today = LocalDate.now();

        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        Map<LocalDate, List<TimeTableModel>> weeklyAppointments =
            startOfWeek.datesUntil(startOfWeek.plusDays(7))
                .collect(Collectors.toMap(
                    date -> date,
                    date -> this.timeTableService.getAppointmentsForDate(date),
                    (oldValue, newValue) -> oldValue,
                    LinkedHashMap::new
                ));

        model.addAttribute("weeklyAppointments", weeklyAppointments);
        model.addAttribute("startOfWeek", startOfWeek);

        return PatientDataProvider.ADMIN_PATH_TEMPLATE + "weekly-calendar";
    }
}
