package com.ckay.bubble.service;

import com.ckay.bubble.model.RegisterRequestDTO;
import com.ckay.bubble.model.User;
import com.ckay.bubble.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//TODO Username normalization bug, Password hashing location (AuthService should only be for auth)

@Service
public class UserService {

    private UserRepository userRepository;
    private AuthService authService;

    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }


    public void registerUser(RegisterRequestDTO registerRequest) {

        //Prevents case-sensitive usernames & emails
        String normalizedUsername = registerRequest.getUsername().toLowerCase();

        if (userRepository.findByUsername(normalizedUsername).isPresent()) {
            throw new IllegalArgumentException("Username is taken");
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPasswordHash(authService.hash(registerRequest));
        userRepository.save(newUser);
    }

}
