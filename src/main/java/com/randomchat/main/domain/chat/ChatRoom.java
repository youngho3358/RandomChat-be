package com.randomchat.main.domain.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String firstUser;

    @Column
    private String secondUser;

    @Column(nullable = false)
    private boolean open;

    public ChatRoom(String nickname) {
        this.firstUser = nickname;
        this.open = true;
    }

    public ChatRoom addUser(String nickname) {
        this.secondUser = nickname;
        return this;
    }

    public ChatRoom closeRoom() {
        this.open = false;
        return this;
    }
}