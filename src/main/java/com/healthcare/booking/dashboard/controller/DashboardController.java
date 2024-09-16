package com.healthcare.booking.dashboard.controller;

import com.healthcare.booking.patient.model.PatientModel;
import com.healthcare.booking.patient.provider.PatientDataProvider;
import com.healthcare.booking.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping({"/admin", "/admin/"})
public class DashboardController {
    @Autowired
    private PatientService patientService;

    @GetMapping()
    public String dashboard(Model model) {
        List<PatientModel> patients = this.patientService.getListPatient(5);
        model.addAttribute("patients", patients);
        model.addAttribute("showColumnAction", false);
        return PatientDataProvider.ADMIN_PATH_TEMPLATE + "dashboard";
    }
}
