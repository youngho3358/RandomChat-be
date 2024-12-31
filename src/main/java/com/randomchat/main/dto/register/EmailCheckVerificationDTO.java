package com.randomchat.main.dto.register;

import lombok.Data;

@Data
public class EmailCheckVerificationDTO {
    private String email;
    private String verificationCode;
}
