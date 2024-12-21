package com.randomchat.main.repository.email;

import com.randomchat.main.domain.email.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification,Long> {
}
