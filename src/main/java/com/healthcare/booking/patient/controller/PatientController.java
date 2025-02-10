package com.healthcare.booking.patient.controller;

import com.healthcare.booking.patient.provider.PatientDataProvider;
import com.healthcare.booking.patient.model.PatientModel;
import com.healthcare.booking.patient.service.PatientService;
import com.healthcare.booking.patient.spec.PatientResponseFilterDTO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping({"admin/patient/management", "admin/patient/management/"})
public class PatientController {
    @Autowired
    private PatientService patientService;
//    @Resource(name = "requestScopedBean")
//    HelloMessageGenerator requestScopedBean;

    @GetMapping
    public String getListPatients(
            @RequestParam(required = false) Map<String, String> filterParams,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            Model model) {

        Page<PatientModel> pagePatients = this.patientService.getListPatientWithFilter(filterParams, page, size);

        model.addAttribute("filters", PatientDataProvider.FILTER_OPTIONS);
        model.addAttribute("patients", pagePatients.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagePatients.getTotalPages());
        model.addAttribute("showColumnAction", true);

        return PatientDataProvider.PATIENT_MANAGEMENT_PATH_TEMPLATE + "all";
    }

    public String searchProducts(@RequestParam(name = "another") String query) {
        return "Query: " + query;
    }


    @GetMapping(value = {"/filter", "/filter/"}, produces = "application/json")
    @ResponseBody
    public PatientResponseFilterDTO filterPatients(@RequestParam(required = false) Map<String, String> filterParams,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "15") int size) {
        Page<PatientModel> pagePatients = this.patientService.getListPatientWithFilter(filterParams, page, size);
//        Map<String, Object> response = new HashMap<>();
//        response.put("patients", pagePatients.getContent());
//        response.put("currentPage", pagePatients.getNumber());
//        response.put("totalPages", pagePatients.getTotalPages());
//        response.put("totalItems", pagePatients.getTotalElements());
//        response.put("pageSize", pagePatients.getSize());
//
//        return response;
        return new PatientResponseFilterDTO(
            pagePatients.getContent(),
            pagePatients.getNumber(),
            pagePatients.getTotalPages(),
            pagePatients.getTotalElements(),
            pagePatients.getSize()
        );
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
            return PatientDataProvider.PATIENT_MANAGEMENT_PATH_TEMPLATE + "detail";
        }
        model.addAttribute("error_message", "There's something wrong while get patient detail!");
        return "redirect:/admin/patient/management";
    }

    @GetMapping({"/delete/{patient_id}", "/delete/{patient_id}/"})
    public String deletePatient(@PathVariable(required = false, name = "patient_id") String patientId, Model model) {
        if (patientId != null) {
            PatientModel patient = this.patientService.getPatientById(Long.valueOf(patientId));
            if (patient != null && patient.getId() != null) {
                this.patientService.deletePatient(Long.valueOf(patientId));
                model.addAttribute("success_message", "Patient with ID (" + patientId + ") is deleted successfully!");
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
//        model.addAttribute("previousMessage", requestScopedBean.getMessage());
//        requestScopedBean.setMessage("Good morning!");
//        model.addAttribute("currentMessage", requestScopedBean.getMessage());
        model.addAttribute("patient", new PatientModel());
        model.addAttribute("title", "Create New Patient");
        return PatientDataProvider.PATIENT_MANAGEMENT_PATH_TEMPLATE + "detail";
    }

    @PostMapping({"/save", "/save/"})
    public String savePatient(@Valid @ModelAttribute("patient") PatientModel patient, Model model) {
        PatientModel savePatient = this.patientService.savePatient(patient);
        if (savePatient == null) {
            model.addAttribute("error_message", "Patient save failed!");
        } else {
            model.addAttribute("message", "Patient saved successfully!");
        }
        return "redirect:/admin/patient/management";
    }
}
