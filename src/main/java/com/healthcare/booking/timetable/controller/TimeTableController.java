package com.healthcare.booking.timetable.controller;

import com.healthcare.booking.patient.api.PaginationResponse;
import com.healthcare.booking.patient.provider.PatientDataProvider;
import com.healthcare.booking.timetable.api.TimeTableResponse;
import com.healthcare.booking.timetable.provider.TimeTableDTO;
import com.healthcare.booking.timetable.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        Map<LocalDate, List<TimeTableDTO>> weeklyAppointments =
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

    @GetMapping({"refresh", "refresh/"})
    public String refresh(Model model) {
        this.timeTableService.clearAllAppointmentsCache();
        return "redirect:/admin/weekly-calendar";
    }
}
