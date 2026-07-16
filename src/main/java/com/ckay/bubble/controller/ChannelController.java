package com.ckay.bubble.controller;

import com.ckay.bubble.model.entity.ChatMessage;
import com.ckay.bubble.repository.ChannelRepository;
import com.ckay.bubble.repository.ChatMessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channel")
@CrossOrigin(origins = "*")
public class ChannelController {

    private final ChatMessageRepository repository;

    public ChannelController(ChatMessageRepository messageHistoryRepository) {
        repository = messageHistoryRepository;
    }

    @GetMapping("/{roomId}/channel-history")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String roomId) {
        if (roomId == null || roomId.isEmpty()) {
            throw new IllegalArgumentException("room does not exist");
        }
        List<ChatMessage> history = repository.findByRoomIdOrderByCreatedAtAsc(roomId);
        return ResponseEntity.ok(history);
    }
}
