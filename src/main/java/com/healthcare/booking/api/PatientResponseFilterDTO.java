package com.healthcare.booking.api;

import com.healthcare.booking.model.PatientModel;
import lombok.Data;

import java.util.List;

@Data
public class PatientResponseFilterDTO {
    private List<PatientModel> patients;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int pageSize;

    public PatientResponseFilterDTO(List<PatientModel> patients, int currentPage, int totalPages, long totalItems, int pageSize) {
        this.patients = patients;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.pageSize = pageSize;
    }
}
