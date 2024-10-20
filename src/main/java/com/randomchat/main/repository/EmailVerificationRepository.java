package com.randomchat.main.repository;

import com.randomchat.main.domain.email.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification,Long> {
}
