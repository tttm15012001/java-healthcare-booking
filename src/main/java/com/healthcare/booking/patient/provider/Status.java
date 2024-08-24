package com.healthcare.booking.patient.provider;

public enum Status {
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

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static String getLabelByCode(int code) {
        for (Status status : values()) {
            if (status.getCode() == code) {
                return status.getLabel();
            }
        }
        return "Unknown";
    }
}