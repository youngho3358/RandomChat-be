package com.randomchat.main.controller.register;

import com.randomchat.main.dto.register.EmailCheckVerificationDTO;
import com.randomchat.main.dto.register.EmailVerificationDTO;
import com.randomchat.main.service.email.EmailVerificationService;
import com.randomchat.main.service.register.RegisterService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/register")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;
    private final RegisterService registerService;

    @PostMapping("/email/verification")
    public ResponseEntity<String> emailVerification(@RequestBody EmailVerificationDTO emailVerificationDTO) throws MessagingException, IOException {

        String email = emailVerificationDTO.getEmail();

        // 1. 이메일 인증이 들어온 이메일 정보로 가입된 계정이 있다면 deny
        if(registerService.checkEmailDuplication(email)) return ResponseEntity.status(409).body("이미 가입된 이메일입니다.");

        // 2. 이메일 인증이 들어온 기준 시간으로 부터 5분 내로 DB 내에 5회 인증 요청이 있다면 deny
        if(emailVerificationService.esExceededLimit(email)) return ResponseEntity.status(429).body("5분 내 요청 횟수가 초과되었습니다.");

        // 3. 이메일 인증용 난수 생성
        String verificationCode = emailVerificationService.createCode();

        // 4. 이메일을 발송한 뒤 이메일 인증 데이터 DB 에 저장
        emailVerificationService.sendEmail(email, verificationCode); // 인증 이메일 발송
        emailVerificationService.saveEmailVerification(email, verificationCode); // DB 에 인증 내용 저장

        return ResponseEntity.ok("이메일이 발송되었습니다.");
    }

    @PostMapping("/email/check/verification")
    public ResponseEntity<String> emailCheckVerification(@RequestBody EmailCheckVerificationDTO emailCheckVerificationDTO) {

        String email = emailCheckVerificationDTO.getEmail();
        String verificationCode = emailCheckVerificationDTO.getVerificationCode();

        // 1. 이메일(email)과 인증코드(verificationCode) 정보를 담은 데이터를 받아서 변수로 저장한 뒤 DB 안에 해당 email 로 인증된 가장 최근 데이터의
        //    verificationCode 정보가 요청온 정보와 일치하는지 확인한 뒤 일치하지 않으면 401 상태코드를 반환, 일치한다면 200 OK 상태코드 반환 및 isVerified 컬럼 값
        //    true 로 변경
        if(emailVerificationService.compareVerificationCode(email, verificationCode)) {
            return ResponseEntity.status(200).body("이메일 인증이 완료되었습니다.");
        }else {
            return ResponseEntity.status(401).body("인증코드가 일치하지 않습니다.");
        }
    }
}
