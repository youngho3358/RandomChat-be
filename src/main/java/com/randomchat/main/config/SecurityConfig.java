package com.randomchat.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .disable() // CSRF 보호 비활성화
                )

                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin() // H2 콘솔 접근 허용
                        )
                )

                .authorizeRequests()
                    // 메모리를 사용하는 H2 데이터베이스의 접속 경로를 오픈
                    // http://localhost:8080/h2-console 경로로 콘솔 접근 가능
                    .requestMatchers("/h2-console/**").permitAll()
                    .anyRequest().authenticated()
                .and();

        return http.build();
    }
}
