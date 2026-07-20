package com.ckay.bubble.controller;

import com.ckay.bubble.model.dto.ChannelDTO;
import com.ckay.bubble.model.entity.Channel;
import com.ckay.bubble.model.entity.ChatMessage;
import com.ckay.bubble.repository.ChannelRepository;
import com.ckay.bubble.repository.ChatMessageRepository;
import com.ckay.bubble.repository.UserRepository;
import com.ckay.bubble.service.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channel")
@CrossOrigin(origins = "*")
public class ChannelController {

    private final ChatMessageRepository repository;
    private final ChannelService channelService;

    public ChannelController(ChatMessageRepository messageHistoryRepository, ChannelService channelService) {
        this.repository = messageHistoryRepository;
        this.channelService = channelService;
    }

    @GetMapping("/{roomId}/channel-history")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String roomId) {
        if (roomId == null || roomId.isEmpty()) {
            throw new IllegalArgumentException("room does not exist");
        }
        //TODO refactor to lean more toward bidirectional DB relationship!
        List<ChatMessage> history = repository.findByRoomIdOrderByCreatedAtAsc(roomId);
        return ResponseEntity.ok(history);
    }

    // TODO Test this endpoint using Spring tests
    @PostMapping("/create-channel")
    public ResponseEntity<Channel> createChannel(@RequestBody ChannelDTO channelDTO) {
        if (channelDTO == null) {
            throw new IllegalArgumentException("channelDTO is null");
        }
        channelService.createChannel(channelDTO.getChannelName(), channelDTO.getOwner());
        return ResponseEntity.ok().build();
    }
}
