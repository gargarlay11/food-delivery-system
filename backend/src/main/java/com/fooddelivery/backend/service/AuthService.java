package com.fooddelivery.backend.service;

import com.fooddelivery.backend.dto.AuthResponse;
import com.fooddelivery.backend.dto.LoginRequest;
import com.fooddelivery.backend.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(
            RegisterRequest request
    );

    AuthResponse login(
            LoginRequest request
    );
}