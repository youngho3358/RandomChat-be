package com.randomchat.main.controller;

import com.randomchat.main.domain.users.Users;
import com.randomchat.main.service.jwt.GetTokenDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final GetTokenDetailsService getTokenDetailsService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/mypage")
    public String test(HttpServletRequest request) {

        Users user = getTokenDetailsService.getUserDetails();

        return "userDetails >>> " + user.getRole() + ", " + user.getEmail() + ", " + user.getNickname() + ", " + user.getGender();

    }
}
