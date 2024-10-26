package com.randomchat.main.controller.register;

import com.randomchat.main.service.register.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class NicknameDuplicationController {

    private final RegisterService registerService;

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<String> isNicknameDuplication(@PathVariable String nickname) {
        if(registerService.checkNicknameDuplication(nickname)) return ResponseEntity.status(409).body("사용중인 닉네임입니다.");

        return ResponseEntity.status(200).body("사용 가능한 닉네임입니다.");
    }

}
