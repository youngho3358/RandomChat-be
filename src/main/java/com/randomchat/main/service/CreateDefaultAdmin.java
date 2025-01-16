package com.randomchat.main.service;

import com.randomchat.main.domain.users.Users;
import com.randomchat.main.repository.users.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateDefaultAdmin {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsersRepository usersRepository;

    @PostConstruct
    public void createDefaultAdmin() {

        // 관리자 계정이 존재하는 경우 관리자 계정 생성을 패스
        if(usersRepository.findByEmail("admin").isPresent()) return;

        String email = "admin";
        String password = bCryptPasswordEncoder.encode("admin");
        String nickname = "admin";

        Users user = new Users();
        Users admin = user.createAdmin(email, password, nickname);

        usersRepository.save(admin);
    }

//    @PostConstruct
//    public void testCreateUsers() {
//        for(int i=0; i<100000; i++) {
//           Users user = new Users();
//            Users testUser = user.createUser("a" + i, "a" + i, "a" + i, "USER", "MALE");
//            usersRepository.save(testUser);
//        }
//    }

}