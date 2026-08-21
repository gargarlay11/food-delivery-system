package com.fooddelivery.backend.exception;

public class InvalidCredentialsException
        extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}