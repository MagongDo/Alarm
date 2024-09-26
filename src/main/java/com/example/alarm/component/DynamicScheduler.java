package com.example.alarm.component;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class DynamicScheduler {
    private final TaskScheduler taskScheduler;

    // 스케줄 작업을 저장할 맵 (알림 ID와 해당 스케줄 작업을 매핑)
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public DynamicScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    // 새 작업을 추가
    public void scheduleTask(Long notificationId, Runnable task, Date executionTime) {
        // 기존 작업이 있다면 취소
        cancelTask(notificationId);

        // 새 작업을 등록하고 저장
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, executionTime);
        scheduledTasks.put(notificationId, scheduledFuture);
    }

    // 기존 작업을 취소
    public void cancelTask(Long notificationId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(notificationId);
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }
        scheduledTasks.remove(notificationId);
    }
}
