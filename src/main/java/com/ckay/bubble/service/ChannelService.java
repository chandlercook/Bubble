package com.ckay.bubble.service;

import com.ckay.bubble.model.entity.Channel;
import com.ckay.bubble.repository.ChannelRepository;
import com.ckay.bubble.repository.UserRepository;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {

    // method to create a new channel

    // Requirements
    // - Authorization
    // - A name for the channel
    // - Link the owner of the channel to the User
    // -


    final private UserRepository userRepository;
    final private ChannelRepository channelRepository;

    public ChannelService(UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    public void createChannel(String channelName, String user){
        if (userRepository.findByUsername(user).isEmpty()){
            throw new UsernameNotFoundException(user);
        }

        Channel channel = new Channel();
        channel.setOwner(userRepository.findByUsername(user).get());
        channel.setName(channelName);
        channelRepository.save(channel);
    }
}
