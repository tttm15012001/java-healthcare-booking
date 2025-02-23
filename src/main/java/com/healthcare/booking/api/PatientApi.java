package com.healthcare.booking.api;

import com.healthcare.booking.model.PatientModel;
import com.healthcare.booking.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping({"/api/patients", "/api/patients/"})
@PropertySource("classpath:application-test.properties")
public class PatientApi {

    @Autowired
    private PatientService patientService;

    @Autowired
    private Environment environment;

    @GetMapping
    public PaginationResponse<?> getPatientsWithPagination(
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @RequestParam(required = false, defaultValue = "0") Integer size
    ) {
        PaginationRequest paginationRequest;
        environment.getProperty("spring.datasource.username");

        if (page != null && size != null) {
            paginationRequest = new PaginationRequest(page, size);
        } else {
            paginationRequest = new PaginationRequest(0, 0);
        }
        return this.patientService.getAllPatientsWithPagination(paginationRequest);
    }

    @GetMapping("detail/{patient_id}")
    public PatientResponse getPatientDetails(@PathVariable("patient_id") Integer id) {
        if (id != null && id > 0) {
            return this.patientService.getPatientDetails(id);
        }

        return null;
    }

    @GetMapping("/test")
    public List<PatientModel> getAllPatients() {
        return this.patientService.getListPatient(null);
    }

    @GetMapping("/coffee")
    public PatientResponse getCoffee() {
        PatientResponse patientResponse = new PatientResponse();
        patientResponse.setFullName("test");
        patientResponse.setCreatedAt(LocalDateTime.now());
        return patientResponse;
    }
}
