package com.example.alarm.service;

import com.example.alarm.component.DynamicScheduler;
import com.example.alarm.entity.CoustomAlarmNotification;
import com.example.alarm.repository.CoustomAlarmNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class CoustomAlarmService {
    @Autowired
    private DynamicScheduler dynamicScheduler;

    @Autowired
    private CoustomAlarmNotificationRepository coustomAlarmNotificationRepository;

    private final RedisTemplate<String, String> redisTemplate;

    // 알림 생성 시 스케줄링
    public void createOrUpdateNotification(CoustomAlarmNotification notification) {
        // 알림을 데이터베이스에 저장
        CoustomAlarmNotification savedNotification = coustomAlarmNotificationRepository.save(notification);

        // 새롭게 알림 스케줄링
        scheduleNotification(savedNotification);
    }

    // 알림을 스케줄링하는 메소드
    public void scheduleNotification(CoustomAlarmNotification notification) {
        LocalTime notificationTime = notification.getReserveAt();
        Set<DayOfWeek> notificationDays = parseDays(notification.getNotificationDays());

        // 알림을 실행할 작업 정의
        Runnable notificationTask = () -> {
            DayOfWeek today = LocalDate.now().getDayOfWeek();
            if (notificationDays.contains(today)) {
                sendNotificationToRedisStream(notification.getUser());
            }
        };

        // 다음 실행 시간을 계산하여 스케줄링
        Date nextExecutionTime = getNextExecutionTime(notificationTime);
        dynamicScheduler.scheduleTask(notification.getId(), notificationTask, nextExecutionTime);
    }

    // Redis Streams로 알림을 전송하는 메소드
    private void sendNotificationToRedisStream(Long userId) {
        StreamOperations<String, Object, Object> streamOps = redisTemplate.opsForStream();
        Map<String, Object> fields = new HashMap<>();
        fields.put("userId", userId);
        fields.put("message", "Your scheduled notification");

        streamOps.add(MapRecord.create("notifications", fields));
    }

    // 다음 실행 시간을 계산하는 메소드
    private Date getNextExecutionTime(LocalTime notificationTime) {
        LocalTime now = LocalTime.now();
        LocalTime nextRunTime = notificationTime.isAfter(now) ? notificationTime : notificationTime.plusHours(24);
        return Date.from(nextRunTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    // 요일 문자열을 Set<DayOfWeek>로 변환하는 메소드
    private Set<DayOfWeek> parseDays(String days) {
        return Arrays.stream(days.split(","))
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());
    }

    // 알림 취소 (삭제할 때 호출)
    public void cancelNotification(Long notificationId) {
        dynamicScheduler.cancelTask(notificationId);
        coustomAlarmNotificationRepository.deleteById(notificationId);
    }
}
