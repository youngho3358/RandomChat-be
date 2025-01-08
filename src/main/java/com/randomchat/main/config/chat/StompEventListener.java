package com.randomchat.main.config.chat;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StompEventListener {
    private static final Logger logger = LoggerFactory.getLogger(StompEventListener.class);

    // 현재 접속 중인 유저 세션 관리
    private final Set<String> activeSessions = Collections.synchronizedSet(new HashSet<>());

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        // 연결 시 유저 sessionId 추가
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        if (sessionId != null) {
            activeSessions.add(sessionId);
            logger.info("New connection: sessionId = {}, total connections = {}", sessionId, activeSessions.size());
        }
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        // 연결 끊길 시 유저 sessionId 삭제
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        if (sessionId != null) {
            activeSessions.remove(sessionId);
            logger.info("Connection closed: sessionId = {}, total connections = {}", sessionId, activeSessions.size());
        }
    }

    public int getActiveUserCount() {
        // 현재 접속 중인 유저 수 반환
        return activeSessions.size();
    }
}