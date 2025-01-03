package com.randomchat.main.controller.backOffice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public String showDashBoardPage() {
        return "dashboard/dashboard";
    }

}
