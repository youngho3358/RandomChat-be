package com.randomchat.main.repository.email;

import com.randomchat.main.domain.email.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification,Long> {

    // 네이티브 쿼리를 사용하여 5분 내에 5회 이상 이메일 인증을 전송한 사용자가 있는지 확인
    @Query(value =  "SELECT COUNT(*) FROM email_verification " +
                    "WHERE email = :email " +
                    "AND attempt_time >= NOW() - INTERVAL 5 MINUTE",
            nativeQuery = true)
    int countRecentAttempts(@Param("email") String email);

    Optional<EmailVerification> findFirstByEmailOrderByIdDesc(String email); // 이메일 인증 기록중 가장 최신 데이터를 불러오는 메소드
}