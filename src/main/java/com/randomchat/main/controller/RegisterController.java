package com.randomchat.main.controller;

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
        boolean registerResult = registerService.register(registerDTO);

        // 이미 가입된 이메일일 경우
        if(!registerResult) return ResponseEntity.status(409)
                .body("already registered email");

        return ResponseEntity.ok()
                .body("register success");
    }
}
