package com.healthcare.booking.patient.service;

import com.healthcare.booking.patient.model.PatientModel;
import com.healthcare.booking.patient.repository.PatientRepository;
import com.healthcare.booking.patient.spec.PatientSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientSpecification patientSpecification;

    public List<PatientModel> getListPatient() {
        return this.patientRepository.findAll();
    }

    public List<PatientModel> getListPatientWithFilter(Map<String, String> filterParams) {
        return this.patientRepository.findAll(patientSpecification.filterBy(filterParams));
    }

    public PatientModel getPatientById(Long id) {
        return this.patientRepository.findById(id).orElse(null);
    }

    public PatientModel savePatient(PatientModel patientModel) {
        return this.patientRepository.save(patientModel);
    }

    public String getFullName(PatientModel patientModel) {
        return patientModel.getFirstName() + " " + patientModel.getLastName();
    }

    public void deletePatient(Long id) {
        this.patientRepository.findById(id).ifPresent(patientModel -> this.patientRepository.delete(patientModel));
    }
}
