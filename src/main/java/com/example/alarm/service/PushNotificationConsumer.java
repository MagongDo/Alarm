/*
package com.example.alarm.service;

import com.example.alarm.entity.Notification;
import com.example.alarm.repository.NotificationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushNotificationConsumer {

    private static final String STREAM_KEY = "push_notifications";

    private final RedisTemplate<String, String> redisTemplate;
    private final NotificationRepository notificationRepository;

    @Autowired
    public PushNotificationConsumer(RedisTemplate<String, String> redisTemplate,
                                    NotificationRepository notificationRepository) {
        this.redisTemplate = redisTemplate;
        this.notificationRepository = notificationRepository;
    }

    @PostConstruct
    public void startConsuming() {0
        new Thread(() -> {
            while (true) {
                List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream()
                        .read( StreamOffset.fromStart(STREAM_KEY));

                if (messages != null) {
                    for (MapRecord<String, Object, Object> message : messages) {
                        MapRecord<String, Object, Object> notificationData = (MapRecord<String, Object, Object>) message.getValue();
                        String recipient = (String) notificationData.getValue().get("recipient");
                         String notificationMessage = (String) notificationData.getValue().get("message");

                        // 알림 처리 (콘솔 출력)
                        System.out.println("Sending notification to: " + recipient);
                        System.out.println("Message: " + notificationMessage);

                        // SQL에서 알림 상태 업데이트
                        Notification notification = notificationRepository
                                .findById(Long.valueOf(message.getId().getValue())).orElse(null);
                        if (notification != null) {
                            notification.setSent(true);
                            notificationRepository.save(notification);
                        }

                        // Redis에서 메시지 삭제
                        redisTemplate.opsForStream().delete(STREAM_KEY, message.getId());
                    }
                }

                // 잠시 대기
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}
*/
