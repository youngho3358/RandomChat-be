package com.randomchat.main.service.chat;

import com.randomchat.main.domain.chat.ChatRoom;
import com.randomchat.main.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final ChatRoomRepository chatRoomRepository;
    private final Map<String, Long> chatRoomList = new ConcurrentHashMap<>();

    public Long enterChatRoom(String nickname) {
        Optional<ChatRoom> findRoom = chatRoomRepository.findFirstBySecondUserIsNullAndOpenTrue();

        if(findRoom.isPresent()) {
            ChatRoom chatRoom = findRoom.get();
            ChatRoom changeChatRoom = chatRoom.addUser(nickname);
            chatRoomRepository.save(changeChatRoom);
            chatRoomList.put(nickname, chatRoom.getId()); // 채팅방 관리 Map 에 닉네임과 채팅방 ID 등록
            return chatRoom.getId();
        }else {
            // 매칭 가능한 방이 없는 경우 새 방 생성
            ChatRoom chatRoom = new ChatRoom(nickname);
            chatRoomRepository.save(chatRoom);
            chatRoomList.put(nickname, chatRoom.getId()); // 채팅방 관리 Map 에 닉네임과 채팅방 ID 등록
            return chatRoom.getId();
        }
    }

    public void closeChatRoom(String channel) {
        String[] parts = channel.split("/");
        Long roomId = Long.parseLong(parts[parts.length - 1]);
        ChatRoom room = chatRoomRepository.findById(roomId).get();
        ChatRoom ClosedChatRoom = room.closeRoom();
        chatRoomRepository.save(ClosedChatRoom);
    }
}
