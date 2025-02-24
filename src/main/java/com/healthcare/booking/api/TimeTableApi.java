package com.healthcare.booking.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.booking.model.DoctorModel;
import com.healthcare.booking.dto.ResponseDto;
import com.healthcare.booking.model.PatientModel;
import com.healthcare.booking.dto.TimeTableDto;
import com.healthcare.booking.model.TimeTableModel;
import com.healthcare.booking.service.TimeTableService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/timetable")
public class TimeTableApi {

    private final TimeTableService timeTableService;

    @Autowired
    public TimeTableApi(TimeTableService timeTableService) {
        this.timeTableService = timeTableService;
    }

    @GetMapping("/filter")
    public PaginationResponse<TimeTableResponse> filterTimeTables(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        PaginationRequest paginationRequest;

        if (page != null || size != null) {
            paginationRequest = new PaginationRequest(page, size);
        } else {
            paginationRequest = new PaginationRequest(0, 0);
        }

        return this.timeTableService.filterTimeTable(paginationRequest);
    }

    @GetMapping
    public PaginationResponse<?> getTimeTablesByDate(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        LocalDateTime startOfDay = null;
        LocalDateTime endOfDay = null;
        PaginationRequest paginationRequest;
        if (date != null) {
            try {
                LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                startOfDay = parsedDate.atStartOfDay();
                endOfDay = startOfDay.plusDays(1);
            } catch (DateTimeParseException e) {

            }
        }

        if (page == null || size == null) {
            paginationRequest = new PaginationRequest(0, 0);
        } else {
            paginationRequest = new PaginationRequest(page, size);
        }

        return this.timeTableService.getTimeTablesByDateWithPagination(paginationRequest, startOfDay, endOfDay);
    }

    @GetMapping("/detail/{time_table_id}")
    public TimeTableResponse getTimeTableById(@PathVariable("time_table_id") Long timeTableId) {
        if (timeTableId != null && timeTableId > 0) {
            return this.timeTableService.getTimeTableDetailById(timeTableId);
        }

        return null;
    }

    @PostMapping("/create")
    public ResponseDto createTimeTable(@RequestBody TimeTableDto timeTableDto) {
        PatientModel patient = new PatientModel();
        patient.setId(timeTableDto.getPatientInfo().getId());

        DoctorModel doctor = new DoctorModel();
        doctor.setId(timeTableDto.getDoctorInfo().getId());

        TimeTableModel timeTable = TimeTableModel.builder()
                .patient(patient)
                .doctor(doctor)
                .patientInformation(timeTableDto.getPatientInformation())
                .referenceInformation(timeTableDto.getReferenceInformation())
                .appointmentType(timeTableDto.getAppointmentType())
                .appointmentTime(timeTableDto.getAppointmentTime())
                .description(timeTableDto.getDescription())
                .build();

        this.timeTableService.saveRequest(timeTable);
        return ResponseDto.builder()
                .isSuccess(true)
                .message("Your request was saved successfully!")
                .build();
    }
}
