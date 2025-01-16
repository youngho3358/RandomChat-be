package com.randomchat.main.controller.backOffice.manageMember;

import com.randomchat.main.dto.backOffice.UserListDTO;
import com.randomchat.main.service.backOffice.SearchMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SearchMemberController {

    private final SearchMemberService searchMemberService;

    @GetMapping("/admin/dashboard/search/member")
    public String showDashBoardPage() {
        return "dashboard/search-member";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard/search/all/member")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchMember(
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {

        // 응답할 Map 객체 생성
        Map<String, Object> data = new HashMap<>();

        // 전체 유저의 수를 찾아오는 메소드
        Long totalUserCount = searchMemberService.getTotalUserCount();

        // 전체 유저 중 특정 페이지로부터 10번째 유저까지의 데이터를 List 로 받아오는 메소드
        Page<UserListDTO> userList = searchMemberService.getUserList(page);

        data.put("totalUserCount", totalUserCount);
        data.put("userList", userList.getContent());

        return ResponseEntity.ok(data);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/dashboard/delete")
    @ResponseBody
    public ResponseEntity<String> deleteMember(@RequestBody Map<String, String> emailBody) {
        String email = emailBody.get("email");
        boolean isDeleted = searchMemberService.deleteMember(email);
        if(isDeleted) {
            return ResponseEntity.ok("회원 삭제 성공");
        }
        return ResponseEntity.status(404).body("회원이 존재하지 않습니다.");
    }

}
