package com.example.alarm.controller;

import com.example.alarm.entity.CoustomAlarmNotification;
import com.example.alarm.service.CoustomAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications/custom")
@RequiredArgsConstructor
public class CoustomAlarmController {


        private final CoustomAlarmService coustomAlarmService;


        // 알림 생성 또는 업데이트
        @PostMapping("/schedule")
        public String scheduleNotification(@RequestBody CoustomAlarmNotification notification) {
            coustomAlarmService.createOrUpdateNotification(notification);
            return "Notification scheduled for " + notification.getReserveAt();
        }

        // 알림 취소
        @DeleteMapping("/cancel/{id}")
        public String cancelNotification(@PathVariable Long id) {
            coustomAlarmService.cancelNotification(id);
            return "Notification canceled with ID: " + id;
        }
}
