package com.ckay.bubble.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Relies on an auto-incrementing database column
    private Long id;

    @Setter
    private String username;

    @Setter
    @Column(name = "password_hash")
    private String passwordHash;

    @OneToMany(mappedBy = "author")
    private List<ChatMessage> chatMessages;

    @OneToMany(mappedBy = "owner")
    private List<Channel> channels;
}
