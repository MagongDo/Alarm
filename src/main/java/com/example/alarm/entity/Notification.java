package com.example.alarm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String recipient;
    private boolean isRead=false;
    @CreatedDate
    private LocalDateTime timestamp;
    public Notification(String message, String recipient) {
        this.message = message;
        this.recipient = recipient;
        this.timestamp = LocalDateTime.now();
    }
}
