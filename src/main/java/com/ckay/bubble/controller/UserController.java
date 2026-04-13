package com.ckay.bubble.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/user/me")
    public String getCurrentUser(Authentication authentication) {
        return "Logged in as: " + authentication.getName();
    }
}
