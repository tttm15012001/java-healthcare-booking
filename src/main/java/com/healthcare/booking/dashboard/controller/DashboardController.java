package com.healthcare.booking.dashboard.controller;

import com.healthcare.booking.patient.model.PatientModel;
import com.healthcare.booking.patient.provider.PatientDataProvider;
import com.healthcare.booking.patient.service.PatientService;
import com.healthcare.booking.report.service.ReportService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping({"/admin", "/admin/"})
public class DashboardController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private ReportService reportService;

    @GetMapping()
    public String dashboard(@RequestParam(value = "startDate", required = false) String startDateStr,
                            @RequestParam(value = "endDate", required = false) String endDateStr,
                            Model model) {
        startDateStr = startDateStr == null ? LocalDate.now().toString() : startDateStr;
        endDateStr = endDateStr == null ? LocalDate.now().toString() : endDateStr;
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        List<PatientModel> patients = this.patientService.getListPatient(5);
        model.addAttribute("patients", patients);
        model.addAttribute("showColumnAction", false);

        Map<String, Double> statusPercentages = reportService.getStatusPercentageByDateRange(startDate, endDate);
        model.addAttribute("statusPercentages", statusPercentages);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return PatientDataProvider.ADMIN_PATH_TEMPLATE + "dashboard";
    }
}
