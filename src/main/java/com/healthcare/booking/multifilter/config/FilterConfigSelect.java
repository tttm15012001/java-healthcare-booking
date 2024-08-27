package com.healthcare.booking.multifilter.config;

import java.util.Map;

public class FilterConfigSelect extends FilterConfig {
    private Map<Integer, String> options;

    public FilterConfigSelect(String fieldName, String filterType, String title, String inputType, Map<Integer, String> options) {
        super(fieldName, filterType, title, inputType);
        this.options = options;
    }

    public Map<Integer, String> getOptions() {
        return this.options;
    }
}
