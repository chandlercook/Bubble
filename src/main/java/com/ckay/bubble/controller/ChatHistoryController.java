package com.ckay.bubble.controller;

import com.ckay.bubble.model.entity.ChatMessage;
import com.ckay.bubble.repository.ChatMessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-history")
@CrossOrigin(origins = "*")
public class ChatHistoryController {

    private final ChatMessageRepository repository;

    public ChatHistoryController(ChatMessageRepository messageHistoryRepository) {
        repository = messageHistoryRepository;
    }

    @GetMapping("/history/messages")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String roomId) {
        List<ChatMessage> history = repository.findByRoomIdOrderByCreatedAtAsc(roomId);
        return ResponseEntity.ok(history);
    }

}
