package com.healthcare.booking.patient.api;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResponse<T> {
    private List<T> items;
    private int totalPages;
    private long totalItems;
    private int size;
    private int page;
}
