package com.example.alarm.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RequiredArgsConstructor
public class NotificationRepositoryTest {
    private final NotificationRepository notificationRepository;

    @Test
    @Transactional
    public void testSave() {
        String messages="잘 들어가는지 본다";


    }
}
