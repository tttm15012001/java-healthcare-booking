package com.healthcare.booking.service;

import com.healthcare.booking.dto.TimeTableDto;
import com.healthcare.booking.api.PaginationRequest;
import com.healthcare.booking.api.PaginationResponse;
import com.healthcare.booking.api.TimeTableResponse;
import com.healthcare.booking.model.TimeTableModel;
import com.healthcare.booking.provider.Status;
import com.healthcare.booking.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TimeTableService {

    private static final String REDIS_KEY_PREFIX = "appointments::";
    private static final String CACHE_NAME = "appointmentsCache";

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Page<TimeTableModel> getTimeTableByPatientId(Long patientId) {
        Pageable pageable = PageRequest.of(0, 5);
        return this.timeTableRepository.findByPatientId(patientId, pageable);
    }

    public void saveRequest(TimeTableModel request) {
        this.timeTableRepository.save(request);
    }

    public List<TimeTableDto> getAppointmentsForDate(LocalDate date) {
        String redisKey = generateRedisKeyForDate(date);

        /* Kiểm tra xem dữ liệu có trong Redis không */
        List<TimeTableDto> cachedAppointments = (List<TimeTableDto>) redisTemplate.opsForValue().get(redisKey);
        if (cachedAppointments != null) {
            return cachedAppointments;
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        List<TimeTableModel> appointments = this.timeTableRepository.findByAppointmentTimeBetweenAndStatusNot(start, end, Status.COMPLETE.getCode());

        /* Chuyển đổi từ Timetable model sang DTO */
        List<TimeTableDto> appointmentDTOs = appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        /* Lưu vào Redis với thời gian sống là 1 giờ */
        redisTemplate.opsForValue().set(redisKey, appointmentDTOs, 1, TimeUnit.HOURS);

        return appointmentDTOs;
    }

    public void clearAppointmentsCacheForDate(LocalDate date) {
        String redisKey = generateRedisKeyForDate(date);
        redisTemplate.delete(redisKey);
    }

    public void clearAllAppointmentsCache() {
        // Nếu cần xóa tất cả cache, bạn cần tìm tất cả các key có tiền tố REDIS_KEY_PREFIX và xóa chúng
        // Điều này tùy thuộc vào cấu trúc key trong Redis và cách quản lý của Redis server
        // Redis không cung cấp lệnh 'keys' trong môi trường production, vì vậy cần cẩn thận với hiệu năng
        Set<String> keys = redisTemplate.keys(REDIS_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys); // Xóa tất cả các cache liên quan đến appointments
        }
    }

    /* Hàm để sinh key Redis dựa trên ngày */
    private String generateRedisKeyForDate(LocalDate date) {
        return REDIS_KEY_PREFIX + date.toString();
    }

    public TimeTableDto convertToDto(TimeTableModel timeTable) {
        return TimeTableDto.builder()
                .patientInfo(this.patientService.buildPatientInfoById(timeTable.getPatient().getId()))
                .doctorInfo(this.doctorService.buildDoctorInfoById(timeTable.getDoctor().getId()))
                .status(timeTable.getStatus())
                .statusLabel(timeTable.getStatusLabel())
                .appointmentTime(timeTable.getAppointmentTime())
                .description(timeTable.getDescription())
                .build();
    }

    public PaginationResponse<TimeTableResponse> getTimeTablesByDateWithPagination(
            PaginationRequest paginationRequest,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    ) {
        List<TimeTableResponse> timeTables = new ArrayList<>();
        Page<TimeTableModel> page;
        if (paginationRequest.getPage() > 0 &&  paginationRequest.getSize() > 0) {
            Pageable pageable = PageRequest.of(paginationRequest.getPage() - 1, paginationRequest.getSize());
            page = this.timeTableRepository.findByAppointmentTimeBetween(startOfDay, endOfDay, pageable);
        } else {
            List<TimeTableModel> timeTableList = this.timeTableRepository.findByAppointmentTimeBetweenWithoutPageable(startOfDay, endOfDay);
            page = new PageImpl<>(timeTableList);
        }

        page.getContent().forEach(item -> timeTables.add(this.buildTimeTableEntityResponse(item)));

        return this.buildResponse(timeTables, page);
    }

    public TimeTableResponse getTimeTableDetailById(Long id) {
        TimeTableModel timeTable = this.timeTableRepository.findById(id).orElse(null);

        return this.buildTimeTableEntityResponse(timeTable);
    }

    public PaginationResponse<TimeTableResponse> filterTimeTable(PaginationRequest paginationRequest) {
        List<TimeTableResponse> timeTable = new ArrayList<>();
        Page<TimeTableModel> timeTablePage;
        int requirePage = paginationRequest.getPage();
        int requireSize = paginationRequest.getSize();

        if (requirePage > 0 && requireSize > 0) {
            Pageable pageable = PageRequest.of(requirePage - 1, requireSize);
            timeTablePage = this.timeTableRepository.findAll(pageable);
            timeTablePage.getContent().forEach((item) -> timeTable.add(this.buildTimeTableEntityResponse(item)));
        } else {
            List<TimeTableModel> timeTables = this.timeTableRepository.findAll();
            timeTables.forEach((item) -> timeTable.add(this.buildTimeTableEntityResponse(item)));
            timeTablePage = new PageImpl<>(timeTables);
        }

        return buildResponse(timeTable, timeTablePage);
    }

    private PaginationResponse<TimeTableResponse> buildResponse(
            List<TimeTableResponse> timeTables,
            Page<TimeTableModel> timeTablePage
    ) {
        PaginationResponse<TimeTableResponse> response = new PaginationResponse<>();
        response.setItems(timeTables);
        response.setTotalPages(timeTablePage.getTotalPages());
        response.setTotalItems(timeTablePage.getTotalElements());
        response.setSize(timeTablePage.getSize());
        response.setPage(timeTablePage.getNumber() + 1);

        return response;
    }

    private TimeTableResponse buildTimeTableEntityResponse(TimeTableModel timeTable) {
        if (timeTable == null) {
            return null;
        }

        TimeTableResponse timeTableResponse = new TimeTableResponse();
        timeTableResponse.setId(timeTable.getId());
        timeTableResponse.setPatientId(timeTable.getPatient().getId());
        timeTableResponse.setDoctorId(timeTable.getDoctor().getId());
        timeTableResponse.setDoctorName(timeTable.getDoctor().getName());
        timeTableResponse.setPatientName(timeTable.getPatient().getFullName());
        timeTableResponse.setAppointmentTime(timeTable.getAppointmentTime().toString());
        timeTableResponse.setDescription(timeTable.getDescription());
        timeTableResponse.setStatusLabel(timeTable.getStatusLabel());

        return timeTableResponse;
    }
}
