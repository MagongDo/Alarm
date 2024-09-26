package com.example.alarm.repository;

import com.example.alarm.entity.CoustomAlarmNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoustomAlarmNotificationRepository extends JpaRepository<CoustomAlarmNotification, Long> {
}
