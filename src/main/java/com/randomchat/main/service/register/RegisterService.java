package com.randomchat.main.service.register;

import com.randomchat.main.domain.users.Users;
import com.randomchat.main.dto.register.RegisterDTO;
import com.randomchat.main.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public void register(RegisterDTO registerDTO) {
        String email = registerDTO.getEmail();
        String password = registerDTO.getPassword();

        // 받아온 비밀번호 정보를 암호화
        registerDTO.setPassword(bCryptPasswordEncoder.encode(registerDTO.getPassword()));

        Users users = new Users();
        Users user = users.createUser(registerDTO);
        usersRepository.save(user);
    }

    public boolean checkEmailDuplication(String email) {
        return usersRepository.existsByEmail(email);
    }

    public boolean checkNicknameDuplication(String nickname) {
        return usersRepository.existsByNickname(nickname);
    }
}
