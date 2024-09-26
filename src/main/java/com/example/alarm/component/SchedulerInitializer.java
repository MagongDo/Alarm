package com.example.alarm.component;

import com.example.alarm.entity.CoustomAlarmNotification;
import com.example.alarm.repository.CoustomAlarmNotificationRepository;
import com.example.alarm.service.CoustomAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerInitializer implements CommandLineRunner {

    @Autowired
    private CoustomAlarmService coustomAlarmService;

    @Autowired
    private CoustomAlarmNotificationRepository coustomAlarmNotificationRepository;

    @Override
    public void run(String... args) throws Exception {
        // 서버 시작 시 모든 알림을 다시 스케줄링
        List<CoustomAlarmNotification> notifications = coustomAlarmNotificationRepository.findAll();
        for (CoustomAlarmNotification notification : notifications) {
            if (notification.getStatus()) {
                coustomAlarmService.scheduleNotification(notification);
            }
        }
    }
}