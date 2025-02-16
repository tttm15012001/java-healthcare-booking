package com.healthcare.booking.patient.service;

import com.healthcare.booking.patient.api.PaginationRequest;
import com.healthcare.booking.patient.api.PaginationResponse;
import com.healthcare.booking.patient.api.PatientResponse;
import com.healthcare.booking.patient.model.PatientModel;
import com.healthcare.booking.patient.repository.PatientRepository;
import com.healthcare.booking.patient.spec.PatientSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientSpecification patientSpecification;

    @Autowired
    public PatientService(PatientRepository patientRepository, PatientSpecification patientSpecification) {
        this.patientRepository = patientRepository;
        this.patientSpecification = patientSpecification;
    }

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

    public PaginationResponse<PatientResponse> getAllPatientsWithPagination(PaginationRequest paginationRequest) {
        List<PatientResponse> patients = new ArrayList<>();
        Page<PatientModel> patientPage;
        int requirePage = paginationRequest.getPage() - 1;
        int requireSize = paginationRequest.getSize();
        if (requirePage > 0 && requireSize > 0) {
            Pageable pageable = PageRequest.of(requirePage, requireSize);
            patientPage = this.patientRepository.findAll(pageable);
            patientPage.getContent().forEach(item -> patients.add(buildPatientResponse(item)));
        } else {
            List<PatientModel> patientList = this.patientRepository.findAll();
            patientList.forEach(item -> patients.add(buildPatientResponse(item)));
            patientPage = new PageImpl<>(patientList);
        }

        return getResponse(patients, patientPage);
    }

    public PatientResponse getPatientDetails(Integer id) {
        PatientModel patient = this.patientRepository.findById(Long.valueOf(id)).orElse(null);
        return buildPatientResponse(patient);
    }

    private static PaginationResponse<PatientResponse> getResponse(
        List<PatientResponse> patients,
        Page<PatientModel> patientPage
    ) {
        PaginationResponse<PatientResponse> response = new PaginationResponse<>();
        response.setItems(patients);
        response.setTotalPages(patientPage.getTotalPages());
        response.setTotalItems(patientPage.getTotalElements());
        response.setSize(patientPage.getSize());
        response.setPage(patientPage.getNumber() + 1);

        return response;
    }

    private static PatientResponse buildPatientResponse(PatientModel patientModel) {
        if (patientModel == null) {
            return null;
        }

        PatientResponse patientResponse = new PatientResponse();
        patientResponse.setId(patientModel.getId());
        patientResponse.setFullName(patientModel.getFullName());
        patientResponse.setStatusLabel(patientModel.getStatusLabel());
        patientResponse.setEmail(patientModel.getEmail());
        patientResponse.setPhoneNumber(patientModel.getPhoneNumber());
        patientResponse.setAddress(patientModel.getAddress());
        patientResponse.setGender(patientModel.getGender());
        patientResponse.setBirthDay(patientModel.getBirthday());

        return patientResponse;
    }
}
