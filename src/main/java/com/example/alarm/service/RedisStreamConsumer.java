package com.example.alarm.service;

import com.example.alarm.entity.Notification;
import com.example.alarm.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedisStreamConsumer {
    private final RedisTemplate<String, String> redisTemplate;
    private final NotificationRepository notificationRepository;

    public void consumerMessagesFromStream(String streamKey) {
        if (streamKey == null) {
            throw new IllegalArgumentException("Stream key must not be null");
        }


        // Redis Stream에서 메시지 읽기
        @SuppressWarnings("unchecked") List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream()
                .read(StreamReadOptions.empty().block(Duration.ofSeconds(2)), StreamOffset.fromStart(streamKey));
        log.info("---------------___________________------------");

        for (MapRecord<String, Object, Object> message : messages) {
            log.info(message);
            String notificationMessage = (String) message.getValue().get("payload");
            log.info(notificationMessage);
            // 알림 데이터를 데이터베이스에 저장
            Notification notification = new Notification(notificationMessage, "recipient");
            notificationRepository.save(notification);
            log.info("---------------------------");
            log.info(notificationMessage);

            // 처리한 메시지를 Redis에서 삭제하거나 ACK 처리
            redisTemplate.opsForStream().delete(streamKey, message.getId());
        }
    }
}
