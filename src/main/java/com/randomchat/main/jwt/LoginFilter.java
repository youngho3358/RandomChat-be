package com.randomchat.main.jwt;

import com.randomchat.main.dto.login.LoginDataJsonMappingDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public LoginDataJsonMappingDTO obtainEmailAndPassword(HttpServletRequest request){
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        try(BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // JSON 문자열 추출
        String jsonString = jsonBuilder.toString();

        // JSON 파싱
        String email = null;
        String password = null;
        LoginDataJsonMappingDTO loginDataJsonMappingDTO = new LoginDataJsonMappingDTO();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            email = jsonObject.getString("email"); // email 키의 값 가져오기
            password = jsonObject.getString("password"); // password 키의 값 가져오기

            loginDataJsonMappingDTO.setEmail(email);
            loginDataJsonMappingDTO.setPassword(password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return loginDataJsonMappingDTO;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDataJsonMappingDTO loginDataJsonMappingDTO = obtainEmailAndPassword(request);

        // 요청에서 email 정보와 비밀번호 정보를 추출
        String email = loginDataJsonMappingDTO.getEmail();
        String password = loginDataJsonMappingDTO.getPassword();

        System.out.println("email : " + email);
        System.out.println("password : " + password);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        System.out.println("success");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("fail");
    }
}
