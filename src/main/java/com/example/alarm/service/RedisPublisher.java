package com.example.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedisPublisher {
    private final RedisTemplate<String,String> redisTemplate;

    public void publishToStream(String streamkey,String message){
        //Redis Stream에 알림 메시지를 추가
        ObjectRecord<String,String> record= StreamRecords.newRecord()
                .ofObject(message)//메세지 내용
                .withStreamKey(streamkey);//스트림 키 설정


        redisTemplate.opsForStream().add(record);
        log.info(record);
    }
}
