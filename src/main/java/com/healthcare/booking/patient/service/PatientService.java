package com.healthcare.booking.patient.service;

import com.healthcare.booking.patient.model.PatientModel;
import com.healthcare.booking.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    public List<PatientModel> getListPatient() {
        return this.patientRepository.findAll();
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
