package com.healthcare.booking.timetable.provider;

import com.healthcare.booking.utils.EnumUtils;
import com.healthcare.booking.utils.StatusLabelEnum;

import java.util.Map;

public enum Status implements StatusLabelEnum {
    WAITING(0, "Waiting"),
    DIAGNOSING(1, "Diagnosing"),
    TESTING(2, "Testing"),
    COMPLETE(3, "Complete");

    private final int code;
    private final String label;

    Status(int code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public static String getLabelByCode(int code) {
        return EnumUtils.getLabelByCode(Status.class, code);
    }

    public static Map<Integer, String> getAllOptions() {
        return EnumUtils.getAllOptions(Status.class);
    }
}