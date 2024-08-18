package com.healthcare.booking.patient.provider;

import com.healthcare.booking.multifilter.config.FilterConfig;

import java.util.List;

public class PatientDataProvider {
    public static String PATIENT_MANAGEMENT_PATH_TEMPLATE   = "admin/patient/management/";
    public static String MEDICAL_REGISTRATION_PATH_TEMPLATE = "/admin/medical/";

    public static List<FilterConfig> FILTER_OPTIONS = List.of(
        new FilterConfig("firstName", "like"),
        new FilterConfig("lastName", "like"),
        new FilterConfig("email", "like"),
        new FilterConfig("phoneNumber", "like"),
        new FilterConfig("birthday", "equal")
    );
}
