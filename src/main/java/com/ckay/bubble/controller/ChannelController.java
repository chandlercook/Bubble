package com.ckay.bubble.controller;

import com.ckay.bubble.model.entity.ChatMessage;
import com.ckay.bubble.repository.ChannelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channel")
@CrossOrigin(origins = "*")
public class ChannelController {

    private final ChannelRepository repository;

    public ChannelController(ChannelRepository messageHistoryRepository) {
        repository = messageHistoryRepository;
    }

    @GetMapping("/{roomId}/channel-history")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String roomId) {
        List<ChatMessage> history = repository.findByRoomIdOrderByCreatedAtAsc(roomId);
        return ResponseEntity.ok(history);
    }
}
