package com.randomchat.main.dto.register;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String password;
    private String nickname;
    private String gender;
}
