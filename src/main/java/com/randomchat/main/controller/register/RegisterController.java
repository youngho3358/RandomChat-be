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
        // 1. 최종 검증 코드 필요 ( 이메일 중복 여부 체크, 인증 여부 체크 ) - 중복 시 409 코드 리턴, 미인증시 401 코드 리턴
        // 2. 최종 검증 코드 필요 ( 닉네임 중복 여부 체크 ) - 409 코드 리턴 - 중복 시 409 코드 리턴

        ㅋㅋㅋㅋㅋㅋㅋ

        boolean registerResult = registerService.register(registerDTO);

        return ResponseEntity.ok()
                .body("register success");
    }
}
