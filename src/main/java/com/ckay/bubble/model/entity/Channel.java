package com.ckay.bubble.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "channel_id")
    private UUID channelId;

    @Column(name = "name", unique = true)
    private String channelName;


}
