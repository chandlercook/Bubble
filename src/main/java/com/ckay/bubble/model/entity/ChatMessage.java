package com.ckay.bubble.model.entity;


import com.ckay.bubble.model.dto.ChatMessageDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "room_id")
    private String roomId;
    private String sender;
    private String content;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Getters and setters

    public ChatMessage(ChatMessageDTO message) {
        this.roomId = message.getRoomId();
        this.sender = message.getSender();
        this.content = message.getContent();
        this.createdAt = LocalDateTime.now();
    }

    public ChatMessage() {

    }
}
