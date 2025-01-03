package com.randomchat.main.service.backOffice;

import com.randomchat.main.domain.users.Role;
import com.randomchat.main.domain.users.Users;
import com.randomchat.main.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminLoginService {

    private final UsersRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Users adminLogin(String username, String password) {
        Optional<Users> optionalUser = userRepository.findByEmail(username);
        if(optionalUser.isEmpty()) {
            System.out.println("AdminLoginService.class >>> 해당 이메일로 가입된 로그인 정보가 없음");
            return null;
        }else {
            Users user = optionalUser.get();
            if(bCryptPasswordEncoder.matches(password, user.getPassword()) && user.getRole().equals(Role.ADMIN)) {
                System.out.println("AdminLoginService.class >>> admin 권한을 가진 유저(" + user.getEmail() + ") 로그인 성공");
                return user;
            }else {
                System.out.println("AdminLoginService.class >>> 로그인 권한을 가지지 않은 유저가 로그인 시도 : " + user.getEmail());
                return null;
            }
        }
    }
}
