package com.healthcare.booking.timetable.service;

import com.healthcare.booking.timetable.model.TimeTableModel;
import com.healthcare.booking.timetable.provider.TimeTableDTO;
import com.healthcare.booking.timetable.repo.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TimeTableService {

    private static final String REDIS_KEY_PREFIX = "appointments::";

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Page<TimeTableModel> getTimeTableByPatientId(Long patientId) {
        Pageable pageable = PageRequest.of(0, 5);
        return this.timeTableRepository.findByPatientId(patientId, pageable);
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
        List<TimeTableModel> appointments = this.timeTableRepository.findByAppointmentTimeBetween(start, end);

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
}
