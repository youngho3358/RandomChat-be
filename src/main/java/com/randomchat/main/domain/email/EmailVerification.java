package com.randomchat.main.domain.email;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerification {
    public EmailVerification(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.isVerified = false;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String email;

    @Column
    private String verificationCode;

    @Column(nullable = false)
    private boolean isVerified;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime attemptTime;

    public EmailVerification changeIsVerified() {
        this.isVerified = true;

        return this;
    }
}
