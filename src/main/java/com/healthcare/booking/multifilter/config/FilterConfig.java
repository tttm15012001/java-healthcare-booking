package com.healthcare.booking.multifilter.config;

public class FilterConfig {
    private String fieldName;
    private String filterType;

    public FilterConfig(String fieldName, String filterType) {
        this.fieldName = fieldName;
        this.filterType = filterType;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getFilterType() {
        return this.filterType;
    }
}
