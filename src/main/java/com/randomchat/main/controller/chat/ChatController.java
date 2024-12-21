package com.randomchat.main.controller.chat;

import com.randomchat.main.service.chat.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enter")
public class ChatController {

    private final MatchingService matchingService;

    @PostMapping("/enterChatRoom")
    public ResponseEntity<Map<String, Long>> enterChatRoom(@RequestBody String nickname) {
        Long chatUUID = matchingService.enterChatRoom(nickname);
        Map<String, Long> response = new HashMap<>();
        response.put("roomId", chatUUID);
        return ResponseEntity.ok(response); // OK 상태코드와 함께 생성된 방의 UUID 를 반환한다.
    }

}
