package com.healthcare.booking.patient.provider;

import com.healthcare.booking.multifilter.config.FilterConfig;
import com.healthcare.booking.multifilter.config.FilterConfigSelect;

import java.util.List;

public class PatientDataProvider {
    public static String PATIENT_MANAGEMENT_PATH_TEMPLATE   = "admin/patient/management/";
    public static String MEDICAL_REGISTRATION_PATH_TEMPLATE = "/admin/medical/";
    public static String ADMIN_PATH_TEMPLATE = "admin/";

    public static List<FilterConfig> FILTER_OPTIONS = List.of(
        new FilterConfig("fullName", "like", "Full Name", "text"),
        new FilterConfig("patientCode", "like", "Patient Code", "text"),
        new FilterConfigSelect("status", "equal", "Status", "select", Status.getAllOptions()),
        new FilterConfig("phoneNumber", "like", "Phone Number", "text")
//        new FilterConfig("birthday", "equal", "Date of birth", "date")
    );
}
