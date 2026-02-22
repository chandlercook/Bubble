package com.ckay.bubble.service;

import com.ckay.bubble.model.dto.RegisterRequestDTO;
import com.ckay.bubble.model.entity.User;
import com.ckay.bubble.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private UserRepository userRepository;
    private AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    public String hash(RegisterRequestDTO registerRequest) {
        return passwordEncoder.encode(registerRequest.getPassword());
    }

    public void registerUser(RegisterRequestDTO registerRequest) {

        // Prevents case-sensitive usernames & emails
        String normalizedUsername = registerRequest.getUsername().toLowerCase();

        // Check if username exists
        if (userRepository.findByUsername(normalizedUsername).isPresent()) {
            throw new IllegalArgumentException("Username is taken");
        }

        User newUser = new User();
        newUser.setUsername(normalizedUsername);
        newUser.setPasswordHash(hash(registerRequest));
        userRepository.save(newUser);
    }

}
