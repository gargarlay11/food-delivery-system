package com.fooddelivery.backend.service;

import com.fooddelivery.backend.dto.AuthResponse;
import com.fooddelivery.backend.dto.ForgotPasswordRequest;
import com.fooddelivery.backend.dto.ForgotPasswordResponse;
import com.fooddelivery.backend.dto.LoginRequest;
import com.fooddelivery.backend.dto.RegisterRequest;
import com.fooddelivery.backend.dto.ResetPasswordRequest;

public interface AuthService {

    AuthResponse register(
            RegisterRequest request
    );

    AuthResponse login(
            LoginRequest request
    );

    ForgotPasswordResponse forgotPassword(
            ForgotPasswordRequest request
    );

    void resetPassword(
            ResetPasswordRequest request
    );
}