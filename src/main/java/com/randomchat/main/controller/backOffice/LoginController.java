package com.randomchat.main.controller.backOffice;

import com.randomchat.main.domain.users.Users;
import com.randomchat.main.jwt.JWTUtil;
import com.randomchat.main.service.backOffice.AdminLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AdminLoginService adminLoginService;
    private final JWTUtil jwtUtil;

    @GetMapping("/admin/login")
    public String showLoginPage() {
        // template 디렉토리 하위의 login.html 렌더링
        return "login/login";
    }

    // 로그인 처리
    @PostMapping("/admin/login")
    public ResponseEntity<String> handleLogin(@RequestParam("username") String username,
                                              @RequestParam("password") String password,
                                              HttpServletResponse response) {

        Users user = adminLoginService.adminLogin(username, password);

        if(user != null) {
            // jwt 생성하여 반환해야 함
            String jwtToken = jwtUtil.createJwt(user.getEmail(), user.getNickname(), user.getRole().name(), user.getGender().name(), 60*60*1000L);
            response.addHeader("Authorization", "Bearer " + jwtToken);

            return ResponseEntity.ok("Success login");
        }else {
            // 로그인 실패
            return ResponseEntity.status(401).body("Fail login");
        }

    }
}
