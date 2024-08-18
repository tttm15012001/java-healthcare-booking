package com.healthcare.booking.patient.provider;

import com.healthcare.booking.multifilter.config.FilterConfig;

import java.util.List;

public class PatientDataProvider {
    public static String PATIENT_MANAGEMENT_PATH_TEMPLATE   = "admin/patient/management/";
    public static String MEDICAL_REGISTRATION_PATH_TEMPLATE = "/admin/medical/";

    public static List<FilterConfig> FILTER_OPTIONS = List.of(
        new FilterConfig("firstName", "like", "First Name", "text"),
        new FilterConfig("lastName", "like", "Last Name", "text"),
        new FilterConfig("email", "like", "Email", "text"),
        new FilterConfig("phoneNumber", "like", "Phone Number", "text"),
        new FilterConfig("birthday", "equal", "Date of birth", "date")
    );
}
