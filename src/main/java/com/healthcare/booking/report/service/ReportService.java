package com.healthcare.booking.report.service;

import com.healthcare.booking.timetable.provider.Status;
import com.healthcare.booking.timetable.provider.StatusCount;
import com.healthcare.booking.timetable.repo.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ReportService {

    private static final String REDIS_KEY_PREFIX = "report::";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private TimeTableRepository timeTableRepository;

    public Map<String, Double> getStatusPercentageByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        long totalAppointments = timeTableRepository.countByAppointmentTimeBetween(startDateTime, endDateTime);
        List<StatusCount> statusCounts = timeTableRepository.countStatusesBetween(startDateTime, endDateTime);
        Map<String, Double> statusPercentages = new HashMap<>();

        if (totalAppointments > 0) {
            for (StatusCount statusCount : statusCounts) {
                int statusCode = statusCount.getStatus();
                long count = statusCount.getCount();
                String statusLabel = Status.getLabelByCode(statusCode);
                double percentage = (count / (double) totalAppointments) * 100;
                statusPercentages.put(statusLabel, percentage);
            }
        } else {
            statusPercentages.put("Waiting", 0.0);
            statusPercentages.put("Diagnosing", 0.0);
            statusPercentages.put("Testing", 0.0);
            statusPercentages.put("Complete", 0.0);
        }

        return statusPercentages;
    }

    /* xóa cache theo loại report và thời gian */
    public void clearReportCache(String period, String reportType) {
        String cacheKey = this.generateRedisKey(reportType, period);
        redisTemplate.delete(cacheKey);
    }

    /* xóa toàn bộ cache của báo cáo */
    public void clearAllReportCache() {
        Set<String> keys = redisTemplate.keys(REDIS_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public String generateRedisKey(String reportType, String period) {
        return REDIS_KEY_PREFIX + reportType + "::" + period;
    }
}
