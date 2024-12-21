package com.randomchat.main.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMessageDTO {
    private String type;
    private String sender;
    private String message;
}
