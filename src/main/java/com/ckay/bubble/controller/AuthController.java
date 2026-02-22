package com.ckay.bubble.controller;

import com.ckay.bubble.model.dto.LoginRequestDTO;
import com.ckay.bubble.model.dto.RegisterRequestDTO;
import com.ckay.bubble.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authManager, UserService userService) {
        this.authManager = authManager;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO registerRequest) {
        try {
            userService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Registration failed", "details", e.getMessage()));
        }
    }

        /*
            * -- Authentication Object in Spring Security --
            * AuthenticationManager receives credentials & calls CustomUserDetailsService
            * load user from DB
            * Spring automatically compares raw vs hashed password using PasswordEncoder
            * If correct → builds a fully populated Authentication object
         */
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
            try {
                Authentication authentication = authManager.authenticate(
                        new UsernamePasswordAuthenticationToken( // Spring Security class, holds principal
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );

                String token = jwtUtil.generateToken(authentication);
                return ResponseEntity.ok(Map.of("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "invalid username or password"));
            }
        }
}
