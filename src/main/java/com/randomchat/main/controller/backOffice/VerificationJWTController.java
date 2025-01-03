package com.randomchat.main.controller.backOffice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationJWTController {
    @PostMapping("/verification/jwt")
    public ResponseEntity<String> verificationJWT() {
        return ResponseEntity.status(200).body("ADMIN Token");
    }
}
