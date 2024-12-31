package com.randomchat.main.controller.register;

import com.randomchat.main.dto.register.RegisterDTO;
import com.randomchat.main.service.register.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {

        // 1. 최종 검증 코드 필요 ( 이메일 중복 여부 체크 ) - 중복 시 409 코드 리턴
        if(registerService.checkEmailDuplication(registerDTO.getEmail())) return ResponseEntity.status(409).body("이미 가입된 이메일입니다.");

        // 2. 최종 검증 코드 필요 ( 닉네임 중복 여부 체크 ) - 중복 시 409 코드 리턴
        if(registerService.checkNicknameDuplication(registerDTO.getNickname())) return ResponseEntity.status(409).body("이미 가입된 닉네임입니다.");

        // 3. 최종 검증 코드 필요 ( 이메일 인증 여부 체크 ) - 미인증 시 401 코드 리턴
        if(registerService.checkEmailVerification(registerDTO.getEmail())) return ResponseEntity.status(401).body("이메일이 인증되지 않았습니다.");

        // 4. 최종 회원가입 로직 실행 ( DB insert )
        registerService.register(registerDTO);

        return ResponseEntity.ok()
                .body("register success");
    }
}
