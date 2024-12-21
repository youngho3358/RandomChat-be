package com.randomchat.main.controller.chat;

import com.randomchat.main.dto.chat.ReceiveMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    @MessageMapping("/send/{roomId}")
    @SendTo("/room/{roomId}")
    public ReceiveMessageDTO sendMessage(ReceiveMessageDTO receiveMessageDTO) {
        return receiveMessageDTO;
    }

}
