package com.randomchat.main.dto.backOffice;

import com.randomchat.main.domain.users.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserListDTO {
    private String email;
    private String nickname;
    private Gender gender;
}
