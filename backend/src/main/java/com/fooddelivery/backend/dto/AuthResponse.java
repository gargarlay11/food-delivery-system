package com.fooddelivery.backend.dto;

import java.util.Set;

public class AuthResponse {

    private String token;

    private String tokenType;

    private Long userId;

    private String username;

    private String email;

    private Set<String> roles;

    public AuthResponse(
            String token,
            String tokenType,
            Long userId,
            String username,
            String email,
            Set<String> roles
    ) {
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }
}