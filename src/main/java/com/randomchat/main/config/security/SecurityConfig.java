package com.randomchat.main.config.security;

import com.randomchat.main.jwt.JWTFilter;
import com.randomchat.main.jwt.JWTUtil;
import com.randomchat.main.jwt.LoginFilter;
import com.randomchat.main.repository.users.UsersRepository;
import com.randomchat.main.service.jwt.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // AuthenticationManager 가 인자로 받을 AuthenticationConfiguration 객체 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final UsersRepository usersRepository;
    private final CustomUserDetailsService customUserDetailsService;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // AuthenticationManager Bean 등록
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 폼 로그인 비활성화
                .formLogin(formLogin -> formLogin.disable())

                // HTTP Basic 인증 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())

                // CSRF 보호 비활성화 (Token 인증 방식에서는 불필요)
                .csrf(csrf -> csrf.disable())

                // CORS 커스텀을 적용하는 메소드
                .cors(c -> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOriginPatterns(List.of("*"));
                        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                        config.setAllowedHeaders(List.of("*"));
                        config.setExposedHeaders(List.of("*"));

                        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
                        corsSource.registerCorsConfiguration("/**", config);
                        return config;
                    };
                    c.configurationSource(source);
                })

                // 세션 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // H2 콘솔 접근 허용
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )

                // 경로별 권한 인증 설정
                .authorizeRequests(auth -> auth
                    // 메모리를 사용하는 H2 데이터베이스의 접속 경로를 오픈
                    // http://localhost:8080/h2-console 경로로 콘솔 접근 가능
                    .requestMatchers("/h2-console/**", "/login", "/register/**").permitAll()
                    .requestMatchers("/chat/**", "/room/**", "/send/**", "/enter/**").permitAll() // 채팅 관련 엔드포인트 접속 허용 설정
                    .requestMatchers("/admin/login", "/css/**").permitAll() // 백오피스 관련 엔드포인트 접속 허용
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .anyRequest().authenticated()
                )

                .addFilterAfter(new JWTFilter(jwtUtil), LoginFilter.class)

                // 커스텀 로그인 필터를 등록
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, usersRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
