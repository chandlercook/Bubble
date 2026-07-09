package com.ckay.bubble.controller;


import com.ckay.bubble.model.dto.ChatMessageDTO;
import com.ckay.bubble.model.entity.ChatMessage;
import com.ckay.bubble.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    /*
    *

    == Create actual tests with Spring if possible/needed for JWT ==

    -- TODO --
    * Work on displaying chat room history after closing tab (persistent storage already set up)
    * Page loads
    *
    -> determine current roomId, like "123"
    -> fetch existing messages for that room over HTTP
    -> render them into the <ul>
    -> connect WebSocket
    -> subscribe to /topic/messages/123
    -> append new live messages as they arrive
    *
    * -> Method to create new rooms (channels)
    * -> Having two channels or chatrooms open at once, and switching between both
    * */


    private final ChatMessageRepository messageRepository;
    public ChatController(ChatMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Client -> Server: Client sends a message, goes to this controller via @MessageMapping
    @MessageMapping("/chat/{roomId}") // handles incoming messages, binds roomId
    @SendTo("/topic/messages/{roomId}") // broadcasts returned message to clients subbed to {roomId}
    public ChatMessageDTO sendMessage(ChatMessageDTO message, Principal principal, @DestinationVariable String roomId) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new AccessDeniedException("You must be authenticated to chat");
        }

        message.setRoomId(roomId);
        message.setSender(principal.getName()); // link each message to a sender
        messageRepository.save(new ChatMessage(message));
        return message;
    }
}

/*
   == Flow ==

    send to sever:
    /app/chat/123

    controller receives:
    @MessageMapping("/chat/{roomId}")

    server publishes:
    @SendTo("/topic/messages/{roomId}")

    clients receive from:
    /topic/messages/123

    @MessageMapping = route matcher
    @DestinationVariable = route value extractor
    @SendTo = outgoing broadcaster
 */