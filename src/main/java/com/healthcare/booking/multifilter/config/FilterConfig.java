package com.healthcare.booking.multifilter.config;

public class FilterConfig {
    private String fieldName;
    private String filterType;
    private String title;
    private String inputType;

    public FilterConfig(String fieldName, String filterType, String title, String inputType) {
        this.fieldName = fieldName;
        this.filterType = filterType;
        this.title = title;
        this.inputType = inputType;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getFilterType() {
        return this.filterType;
    }

    public String getTitle() {
        return this.title;
    }

    public String getInputType() {
        return this.inputType;
    }
}
