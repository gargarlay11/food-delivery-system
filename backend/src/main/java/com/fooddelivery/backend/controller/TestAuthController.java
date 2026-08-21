package com.fooddelivery.backend.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestAuthController {

    @GetMapping("/protected")
    public Map<String, Object> protectedEndpoint(
            Authentication authentication
    ) {

        return Map.of(
            "message",
            "You are authenticated",

            "user",
            authentication.getName(),

            "authorities",
            authentication.getAuthorities()
        );
    }
}