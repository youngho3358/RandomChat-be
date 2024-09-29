package com.randomchat.main.domain;

import jakarta.persistence.*;

@Entity
public class Users {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String role;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
