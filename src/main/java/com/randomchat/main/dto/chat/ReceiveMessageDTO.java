package com.randomchat.main.dto.chat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReceiveMessageDTO {
    private String sender;
    private String message;
}
