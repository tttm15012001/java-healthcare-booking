package com.healthcare.booking.patient.api;

import lombok.Data;

@Data
public class PaginationRequest {
    private Integer page;
    private Integer size;

    public PaginationRequest(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }
}
