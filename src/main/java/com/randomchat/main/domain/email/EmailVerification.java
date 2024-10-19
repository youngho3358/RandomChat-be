package com.randomchat.main.domain.email;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
public class EmailVerification {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String email;

    @Column(nullable = false)
    private boolean isVerified;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime attemptTime;
}
