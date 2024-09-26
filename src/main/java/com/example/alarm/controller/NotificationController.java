package com.example.alarm.controller;

import com.example.alarm.service.RedisPublisher;
import com.example.alarm.service.RedisStreamConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/notifications")
public class NotificationController {

    private final RedisPublisher redisStreamPublisher;
    private final RedisStreamConsumer redisStreamConsumer;



    // 알림 메시지를 Redis Streams에 발행
    @PostMapping("/publish")
    public String publishNotification(@RequestParam String message) {
        redisStreamPublisher.publishToStream("notificationStreams", message);
        return "Message published to Redis Stream.";
    }

    // Redis Streams에서 메시지를 읽어와 데이터베이스에 저장
    @PostMapping("/consume")
    public String consumeNotifications() {
        redisStreamConsumer.consumerMessagesFromStream("notificationStreams");

        return "Messages consumed and stored in DB.";
    }
}