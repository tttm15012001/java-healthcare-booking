package com.healthcare.booking.patient.controller;

import com.healthcare.booking.patient.model.PatientModel;
import com.healthcare.booking.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping({"admin/patient/management", "admin/patient/management/"})
public class PatientController {
    private final String PATH_TEMPLATE = "admin/patient/management/";
    @Autowired
    private PatientService patientService;

    @GetMapping
    public String getListPatients(Model model) {
        List<PatientModel> listPatients = this.patientService.getListPatient();
        model.addAttribute("patients", listPatients);
        return this.PATH_TEMPLATE + "all";
    }

    @GetMapping({"/view/{patient_id}", "/view/{patient_id}/"})
    public String getPatientDetailById(@PathVariable(required = false) String patient_id, Model model) {
        if (patient_id == null) {
            model.addAttribute("error_message", "Patient ID is required!");
            return "redirect:/admin/patient/management";
        }
        PatientModel patient = this.patientService.getPatientById(Long.valueOf(patient_id));
        if (patient != null && patient.getId() != null) {
            model.addAttribute("patient", patient);
            model.addAttribute("title", "Edit Patient " + this.patientService.getFullName(patient) + " (ID: " + patient.getId() + ")");
            return this.PATH_TEMPLATE + "detail";
        }
        model.addAttribute("error_message", "There's something wrong while get patient detail!");
        return "redirect:/admin/patient/management";
    }

    @GetMapping({"/delete/{patient_id}", "/delete/{patient_id}/"})
    public String deletePatient(@PathVariable(required = false) String patient_id, Model model) {
        if (patient_id != null) {
            PatientModel patient = this.patientService.getPatientById(Long.valueOf(patient_id));
            if (patient != null && patient.getId() != null) {
                this.patientService.deletePatient(Long.valueOf(patient_id));
                model.addAttribute("success_message", "Patient with ID (" + patient_id + ") is deleted successfully!");
            } else {
                model.addAttribute("error_message", "Patient not found!");
            }
        } else {
            model.addAttribute("error_message", "Patient ID is required!");
        }

        return "redirect:/admin/patient/management";
    }

    @GetMapping({"/view/new", "/view/new/"})
    public String newPatient(Model model) {
        model.addAttribute("patient", new PatientModel());
        model.addAttribute("title", "Create New Patient");
        return this.PATH_TEMPLATE + "detail";
    }

    @PostMapping({"/save", "/save/"})
    public String savePatient(@ModelAttribute("patient") PatientModel patient, Model model) {
        PatientModel savePatient = this.patientService.savePatient(patient);
        if (savePatient == null) {
            model.addAttribute("error_message", "Patient save failed!");
        } else {
            model.addAttribute("message", "Patient saved successfully!");
        }
        return "redirect:/admin/patient/management";
    }
}
