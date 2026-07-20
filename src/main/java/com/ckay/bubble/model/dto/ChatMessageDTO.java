package com.ckay.bubble.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDTO {
    private String content;
    private LocalDateTime createdAt;
    private String authorUsername;
    private String roomId;
}
