package com.randomchat.main.controller.register;

import com.randomchat.main.dto.register.EmailVerificationDTO;
import com.randomchat.main.service.email.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/register")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/email/verification")
    public String emailVerification(@RequestBody EmailVerificationDTO emailVerificationDTO) {

        String email = emailVerificationDTO.getEmail();

        // 1. 이메일 인증이 들어온 이메일 정보로 가입된 계정이 있다면 deny
        emailVerificationService.checkEmailDuplication(email);

        // 2. 이메일 인증이 들어온 기준 시간으로 부터 5분 내로 DB 내에 5회 인증 요청이 있다면 deny
        emailVerificationService.esExceededLimit(email);

        // 3. 이메일 인증용 난수 생성
        String verificationCode = emailVerificationService.createCode();

        // 4. 이메일 인증 데이터 DB 에 저장
        emailVerificationService.saveEmailVerification(email, verificationCode);

        return email;
    }
}
