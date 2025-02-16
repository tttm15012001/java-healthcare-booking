package com.healthcare.booking.timetable.service;

import com.healthcare.booking.patient.api.PaginationRequest;
import com.healthcare.booking.patient.api.PaginationResponse;
import com.healthcare.booking.timetable.api.TimeTableResponse;
import com.healthcare.booking.timetable.model.TimeTableModel;
import com.healthcare.booking.timetable.provider.Status;
import com.healthcare.booking.timetable.provider.TimeTableDTO;
import com.healthcare.booking.timetable.repo.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private RedisTemplate<String, Object> redisTemplate;

    public Page<TimeTableModel> getTimeTableByPatientId(Long patientId) {
        Pageable pageable = PageRequest.of(0, 5);
        return this.timeTableRepository.findByPatientId(patientId, pageable);
    }

    public void saveRequest(TimeTableModel request) {
        this.timeTableRepository.save(request);
    }

    public List<TimeTableDTO> getAppointmentsForDate(LocalDate date) {
        String redisKey = generateRedisKeyForDate(date);

        /* Kiểm tra xem dữ liệu có trong Redis không */
        List<TimeTableDTO> cachedAppointments = (List<TimeTableDTO>) redisTemplate.opsForValue().get(redisKey);
        if (cachedAppointments != null) {
            return cachedAppointments;
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        List<TimeTableModel> appointments = this.timeTableRepository.findByAppointmentTimeBetweenAndStatusNot(start, end, Status.COMPLETE.getCode());

        /* Chuyển đổi từ Timetable model sang DTO */
        List<TimeTableDTO> appointmentDTOs = appointments.stream()
                .map(this::convertToDTO)
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

//    @Cacheable(value = CACHE_NAME, key = "#date.toString()")
//    public List<TimeTableDTO> getAppointmentsForDate(LocalDate date) {
//        LocalDateTime start = date.atStartOfDay();
//        LocalDateTime end = date.atTime(23, 59, 59);
//        List<TimeTableModel> appointments = this.timeTableRepository.findByAppointmentTimeBetween(start, end);
//
//        /* Chuyển đổi từ Timetable model sang DTO */
//        return appointments.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @CacheEvict(value = CACHE_NAME, key = "#date.toString()")
//    public void clearAppointmentsCacheForDate(LocalDate date) {
//        /* Spring tự động xóa cache khi gọi phương thức này */
//    }
//
//    // Xóa toàn bộ cache liên quan đến appointments
//    @CacheEvict(value = CACHE_NAME, allEntries = true)
//    public void clearAllAppointmentsCache() {
//        /* Spring sẽ tự động xóa tất cả cache của appointments */
//    }

    public TimeTableDTO convertToDTO(TimeTableModel timeTable) {
        TimeTableDTO dto = new TimeTableDTO();
        dto.setId(timeTable.getId());
        dto.setAppointmentTime(timeTable.getAppointmentTime().toString());
        dto.setStatus(timeTable.getStatus());
        dto.setStatusLabel(timeTable.getStatusLabel());
        dto.setPatientName(timeTable.getPatient().getFullName());
        dto.setDoctorName(timeTable.getDoctor().getName());
        return dto;
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
