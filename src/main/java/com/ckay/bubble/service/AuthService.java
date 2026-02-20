package com.ckay.bubble.service;

import com.ckay.bubble.controller.AuthController;
import com.ckay.bubble.model.RegisterRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    public AuthService(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public String hash(RegisterRequestDTO registerRequest) {
        return passwordEncoder.encode(registerRequest.getPassword());
    }

}
