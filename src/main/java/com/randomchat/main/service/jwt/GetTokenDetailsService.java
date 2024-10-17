package com.randomchat.main.service.jwt;

import com.randomchat.main.domain.users.Users;
import com.randomchat.main.jwt.JWTUtil;
import com.randomchat.main.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetTokenDetailsService {

    private final UsersRepository usersRepository;

    public Users getUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email =  ((UserDetails) principal).getUsername();
            Optional<Users> finduser = usersRepository.findByEmail(email);
            return finduser.get();
        }
        else {
            throw new RuntimeException("GetTokenDetailsService >> 토큰에 해당하는 유저 정보가 존재하지 않습니다.");
        }
    }
}
