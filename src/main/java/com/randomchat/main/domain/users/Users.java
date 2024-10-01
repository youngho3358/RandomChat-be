package com.randomchat.main.domain.users;

import com.randomchat.main.dto.register.RegisterDTO;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public Users createUser(RegisterDTO registerDTO) {
        this.email = registerDTO.getEmail();
        this.password = registerDTO.getPassword();
        this.nickname = registerDTO.getNickname();
        this.role = Role.USER;
        this.gender = Gender.valueOf(registerDTO.getGender().toUpperCase());

        return this;
    }
}