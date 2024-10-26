package com.randomchat.main.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        // appication.yml 의 secret 키 값을 이용해서 jwt 에 사용될 객체 키 값을 생성
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getEmail(String token) {
        // 암호화 객체 키를 이용하여 jwt 토큰 내부의 이메일 정보를 추출
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getNickname(String token) {
        // 암호화 객체 키를 이용하여 jwt 토큰 내부의 이메일 정보를 추출
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname", String.class);
    }

    public String getRole(String token) {
        // 암호화 객체 키를 이용하여 jwt 토큰 내부의 Role 정보를 추출
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getGender(String token) {
        // 암호화 객체 키를 이용하여 jwt 토큰 내부의 성별 정보를 추출
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("gender", String.class);
    }

    public Boolean isExpired(String token) {
        // 암호화 객체 키를 이용하여 jwt 토큰 내부의 토큰 만료 정보를 추출
        // 현재 시간 값과 비교하여 유효한 토큰인지 검증
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String email, String nickname,String role, String gender, Long expiredMs) {

        return Jwts.builder()
                .claim("email", email)
                .claim("nickname", nickname)
                .claim("role", role)
                .claim("gender", gender)
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 토큰 만료 시간
                .signWith(secretKey) // 암호화 객체 키를 사용해서 토큰 내용을 암호화
                .compact();
    }

}
