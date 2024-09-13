package com.healthcare.booking.patient.service;

import com.healthcare.booking.patient.model.PatientModel;
import com.healthcare.booking.patient.repository.PatientRepository;
import com.healthcare.booking.patient.spec.PatientSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientSpecification patientSpecification;

    public List<PatientModel> getListPatient(Integer num) {
        if (num != null) {
            Pageable topFive = PageRequest.of(0, 5);
            return this.patientRepository.select5NewestPatients(topFive);
        }
        return this.patientRepository.findAll();
    }

    public Page<PatientModel> getListPatientWithFilter(Map<String, String> filterParams, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return this.patientRepository.findAll(patientSpecification.filterBy(filterParams), pageable);
    }

    public PatientModel getPatientById(Long id) {
        return this.patientRepository.findById(id).orElse(null);
    }

    public PatientModel savePatient(PatientModel patientModel) {
        return this.patientRepository.save(patientModel);
    }

    public String getFullName(PatientModel patientModel) {
        return patientModel.getFullName();
    }

    public void deletePatient(Long id) {
        this.patientRepository.findById(id).ifPresent(patientModel -> this.patientRepository.delete(patientModel));
    }
}
