package com.randomchat.main.jwt;

import com.randomchat.main.domain.users.Users;
import com.randomchat.main.dto.CustomUserDetails;
import com.randomchat.main.dto.register.RegisterDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰에 대한 정보가 필요 없는 부분은 토큰 검증 과정을 스킵
        String path = request.getRequestURI();
        if (path.startsWith("/chat") || path.startsWith("/room") || path.startsWith("/send") || path.startsWith("/socket")
            || path.startsWith("/register")) {
            filterChain.doFilter(request, response); // 필터 건너뛰기
            return;
        }

        String authorization = request.getHeader("Authorization");
        System.out.println("토큰 정보 >>> " + authorization);

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("토큰 정보가 비었습니다.");
            filterChain.doFilter(request, response);

            return;
        }

        String token = authorization.split(" ")[1];

        // 토큰 소멸시간 검증
        if(jwtUtil.isExpired(token)) {
            System.out.println("토큰이 만료되었습니다. >>> " + jwtUtil.isExpired(token));
            filterChain.doFilter(request, response);

            return;
        }

        String email = jwtUtil.getEmail(token);
        String nickname = jwtUtil.getNickname(token);
        String role = jwtUtil.getRole(token);
        String gender = jwtUtil.getGender(token);

        Users userEntity = new Users();
        Users user = userEntity.createUser(email, "temppassword", nickname, role, gender);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        System.out.println("<<< auth 토큰 생성 전(JWTFilter.class) >>>");

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        System.out.println("권한 정보(JWTFilter.class) >>> " + customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        System.out.println("다음 필터 체인으로 넘긴다...(JWTFilter.class)");

        filterChain.doFilter(request, response);
    }
}
