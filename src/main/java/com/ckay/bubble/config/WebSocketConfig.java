package com.ckay.bubble.config;

import com.ckay.bubble.security.JwtStompChannelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtStompChannelInterceptor jwtStompChannelInterceptor;

    public WebSocketConfig(JwtStompChannelInterceptor jwtStompChannelInterceptor) {
        this.jwtStompChannelInterceptor = jwtStompChannelInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // Tells Spring to use an internal broker to route messages back to clients.
        // Server -> Client: When the server sends data back, it goes through this message broker
        // so it can be broadcasted to all subscribers (in the connection)
        registry.enableSimpleBroker("/topic"); // " /routing prefix + /exampleRoom "

        // Any message sent by the client starting with /app is routed to our @MessageMapping controllers
        // ex: /app/chat -> calls sendMessage() in ChatController.Java
        registry.setApplicationDestinationPrefixes("/app");
    }

    // ws://localhost:8080/ws -> handshake URL!
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // connection endpoint (where client connects)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtStompChannelInterceptor);
    }
}
