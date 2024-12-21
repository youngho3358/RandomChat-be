package com.randomchat.main.config.chat;

import com.randomchat.main.dto.chat.SendMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class ChannelEventListener {
    // 매칭 이벤트를 담당하는 클래스
    // 2명이 같은 채널에 입장 시 그 채널로 메세지 발송

    private final ConcurrentHashMap<String, AtomicInteger> channelSubscribers = new ConcurrentHashMap<>(); // 채널별 구독자 수를 관리하기 위한 맵
    private final SimpMessagingTemplate simpMessagingTemplate;
    public final Map<String, String> sessionRoomNumberList = new ConcurrentHashMap<>(); // 세션과 채널을 매칭하는 맵

    // 구독 이벤트 처리
    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String destination = headers.getDestination(); // 구독 경로
        String sessionId = headers.getSessionId(); // 세션 아이디

        if (destination != null) {
            // 채널의 구독자 수 증가
            int subscriberCount = channelSubscribers
                    .computeIfAbsent(destination, key -> new AtomicInteger(0))
                    .incrementAndGet();
            sessionRoomNumberList.put(sessionId, destination);

            if (subscriberCount == 2) {
                // 구독자가 2명이 되면 매칭 안내
                sendMatchingMessage(destination);
            }else {
                // 구독자가 1명이면 기다리는 중 안내
                sendWaitingMessage(destination);
            }
        }
    }

    public void sendWaitingMessage(String destination) {
        // 구독 시간 대기 0.3초
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        SendMessageDTO message = new SendMessageDTO("waiting", "", "상대방을 기다리는 중입니다.");
        simpMessagingTemplate.convertAndSend(destination, message);
    }

    public void sendMatchingMessage(String destination) {
        // 구독 시간 대기 0.3초
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        SendMessageDTO message = new SendMessageDTO("matched", "", "매칭되었습니다.");
        simpMessagingTemplate.convertAndSend(destination, message);
    }
}
