package com.healthcare.booking.enums;

import com.healthcare.booking.utils.EnumUtils;
import com.healthcare.booking.utils.StatusLabelEnum;

import java.util.Map;

public enum Status implements StatusLabelEnum {
    NEW(0, "New"),
    WAIT_TREATMENT(1, "Waiting For Treatment"),
    PENDING(2, "Treating"),
    WAITING_MEDICINE(3, "Waiting For Medicine"),
    DONE(4, "Done");

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
