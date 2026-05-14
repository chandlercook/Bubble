package com.ckay.bubble.controller;


import com.ckay.bubble.model.dto.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    /*
    * TODO
    * Attach JWT to WebSocket
    * - identify users
    * - send messages as specific users
    * - build real chat system
    *
    * -> Test current JWT WebSocket set up
    * -> Create actual tests with Spring if possible/needed
    * -> Work a little more on a front end, having a simple login screen and basic chat interface
    *
    * -> Method to create new rooms (channels)
    *
    * -> Having two channels or chatrooms open at once, and switching between both
    * */

    // Client -> Server: Client sends a message, goes to this controller via @MessageMapping
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessageDTO sendMessage(ChatMessageDTO message, Principal principal) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new AccessDeniedException("You must be authenticated to chat");
        }
        message.setSender(principal.getName()); // link each message to sender
        return message;
    }
}
