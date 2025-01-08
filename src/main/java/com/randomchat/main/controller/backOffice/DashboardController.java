package com.randomchat.main.controller.backOffice;

import com.randomchat.main.config.chat.StompEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final StompEventListener stompEventListener;

    @GetMapping("/admin/dashboard")
    public String showDashBoardPage() {
        return "dashboard/dashboard";
    }

    @GetMapping("/admin/dashboard/get/randomchat-user-count")
    @ResponseBody
    public Map<String, Integer> getRandomChatUserCount() {
        // 현재 랜덤채팅에 접속한 유저의 수를 리턴하는 메소드
        Map<String, Integer> response = new HashMap<>();
        response.put("count", stompEventListener.getActiveUserCount());
        return response;
    }

}
