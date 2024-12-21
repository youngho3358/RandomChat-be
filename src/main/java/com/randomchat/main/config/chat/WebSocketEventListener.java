package com.randomchat.main.config.chat;

import com.randomchat.main.dto.chat.SendMessageDTO;
import com.randomchat.main.service.chat.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final MatchingService matchingService;
    private final ChannelEventListener channelEventListener;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /*
    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        //소켓 연결시 입장 유저에 대한 sessionId 획득 및 세부 코드 작성
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = stompHeaderAccessor.getSessionId();
    }
     */

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        System.out.println("WebSocketEventListener : 접속 해제");
        // 소켓 연결이 끊길 시 기존 입장 유저 제거
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = stompHeaderAccessor.getSessionId();
        String channel = channelEventListener.sessionRoomNumberList.get(sessionId);

        sendClosedMessage(channel);
    }

    public void sendClosedMessage(String channel) {
        SendMessageDTO message = new SendMessageDTO("leaved", "", "상대방이 대화를 떠났습니다.");
        simpMessagingTemplate.convertAndSend(channel, message);
        matchingService.closeChatRoom(channel);
    }

}
