package com.healthcare.booking.patient.controller;

import com.healthcare.booking.patient.provider.PatientDataProvider;
import com.healthcare.booking.patient.model.PatientModel;
import com.healthcare.booking.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/admin/medical", "/admin/medical/"})
public class MedicalRegistrationController {
    @Autowired
    private PatientService patientService;

    @GetMapping({"/registration/{patient_id}", "/registration/{patient_id}/"})
    public String medicalRegistration(@PathVariable(required = false) String patient_id, Model model) {
        if (patient_id == null) {
            model.addAttribute("error_message", "Patient ID is required");
            return "redirect:/admin/patient/management";
        }

        PatientModel patient = this.patientService.getPatientById(Long.valueOf(patient_id));
        model.addAttribute("patient", patient);
        return PatientDataProvider.MEDICAL_REGISTRATION_PATH_TEMPLATE + "registration";
    }
}
