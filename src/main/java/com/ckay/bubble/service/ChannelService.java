package com.ckay.bubble.service;

import com.ckay.bubble.model.entity.Channel;
import com.ckay.bubble.model.dto.ChannelSummaryDTO;
import com.ckay.bubble.repository.ChannelRepository;
import com.ckay.bubble.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Channel createChannel(String channelName, String username) {
        var owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Channel channel = new Channel();
        channel.setOwner(owner);
        channel.setName(channelName);
        return channelRepository.save(channel);
    }


    /*
    Lazy fetching: When loading an object, don't automatically load a
    certain row yet.
         -> serialization/DTOs controls what gets returned over HTTP

    Without a service-level transaction, channelRepository.findAll() may run
    inside a repository-scoped transaction that ends when the repo method returns

    Then iterating/mapping the results after that transaction/session is closed,
    and the first time we touch channel.getOwner() Hibernate
    tries to lazy-load and can’t → LazyInitializationException
     */

    @Transactional(readOnly = true)
    public List<ChannelSummaryDTO> listChannels() {
        return channelRepository.findAll().stream()
                .map(channel -> new ChannelSummaryDTO(
                        channel.getChannelId(),
                        channel.getName(),
                        channel.getOwner() != null ? channel.getOwner().getUsername() : null
                ))
                .toList();
    }
}
