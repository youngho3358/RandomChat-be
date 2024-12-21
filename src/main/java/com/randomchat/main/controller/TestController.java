package com.randomchat.main.controller;

import com.randomchat.main.domain.users.Users;
import com.randomchat.main.jwt.JWTUtil;
import com.randomchat.main.repository.users.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final UsersRepository usersRepository;
    private final JWTUtil jwtUtil;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/mypage")
    public String test(HttpServletRequest request) {


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email =  ((UserDetails) principal).getUsername();
            Optional<Users> finduser = usersRepository.findByEmail(email);
            Users user = finduser.get();
            System.out.println("userId >>> " + user.getId());
            System.out.println("userEmail >>> " + user.getEmail());
            System.out.println("userNickname >>> " + user.getNickname());
            System.out.println("userGender >>> " + user.getGender());
            System.out.println("userRole >>> " + user.getRole());
        }









        getCurrentUsername(request);
        return "zzzz";
    }

    private String getCurrentUsername(HttpServletRequest request) {
        // HTTP Header에서 Authorization 부분에서 토큰 추출
        final String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);  // "Bearer " 이후의 토큰 값 추출
        }

        System.out.println("토큰 >>> " + token);

        String email = jwtUtil.getEmail(token);
        String nickname = jwtUtil.getNickname(token);
        String gender = jwtUtil.getGender(token);
        String role = jwtUtil.getRole(token);

        // 토큰 값을 기준으로 가져온 데이터를 DTO 에 담아 return 하는 로직 추가 작성 필요

        return token;
    }
}
