package com.randomchat.main.service.register;

import com.randomchat.main.domain.email.EmailVerification;
import com.randomchat.main.domain.users.Users;
import com.randomchat.main.dto.register.RegisterDTO;
import com.randomchat.main.repository.email.EmailVerificationRepository;
import com.randomchat.main.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UsersRepository usersRepository;
    private final EmailVerificationRepository emailVerificationRepository;
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

    public boolean checkEmailVerification(String email) {
        // 이메일 인증 정보를 담고 있는 객체
        Optional<EmailVerification> findEmailVerification = emailVerificationRepository.findFirstByEmailOrderByIdDesc(email);

        // emailVerification 의 값이 false 라면 true 반환, true 라면 false 반환
        // 그 이외의 상황 즉, Optional 의 값이 없는 경우 등등 이면 true 를 반환
        return findEmailVerification.map(emailVerification -> !emailVerification.isVerified()).orElse(true);
    }
}
