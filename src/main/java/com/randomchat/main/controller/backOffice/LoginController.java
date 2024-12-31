package com.randomchat.main.controller.backOffice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/admin/login")
    public String showLoginPage() {
        // template 페이지 하위의 로그인 페이지 렌더링
        return "login";
    }

    // 로그인 처리
    @PostMapping("/admin/login")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Model model) {
        // 관리자 계정 검증 (간단히 하드코딩된 방식)
        if ("admin".equals(username) && "password".equals(password)) {
            return "redirect:/admin/dashboard"; // 로그인 성공 시 대시보드로 이동
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login"; // 실패 시 다시 로그인 페이지로
        }
    }
}
