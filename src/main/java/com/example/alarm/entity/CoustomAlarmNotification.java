package com.example.alarm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;
import java.util.Date;

@Entity
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class CoustomAlarmNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long user;

    private String message;
    private Boolean isRead;
    private Boolean status;
    private LocalTime reserveAt;
    private String notificationDays;
    @CreatedDate
    private Date createdAt;

}
