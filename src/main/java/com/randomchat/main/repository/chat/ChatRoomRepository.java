package com.randomchat.main.repository.chat;

import com.randomchat.main.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findFirstBySecondUserIsNullAndOpenTrue();
}
