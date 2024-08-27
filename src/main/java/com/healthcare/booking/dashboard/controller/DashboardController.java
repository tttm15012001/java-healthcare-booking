package com.healthcare.booking.dashboard.controller;

import com.healthcare.booking.patient.provider.PatientDataProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/admin", "/admin/"})
public class DashboardController {

    @GetMapping()
    public String dashboard() {
        return PatientDataProvider.ADMIN_PATH_TEMPLATE + "dashboard";
    }
}
