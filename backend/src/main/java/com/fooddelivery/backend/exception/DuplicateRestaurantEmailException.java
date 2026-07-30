package com.fooddelivery.backend.exception;

public class DuplicateRestaurantEmailException extends RuntimeException {

    public DuplicateRestaurantEmailException(String email) {
        super("Restaurant email already exists: " + email);
    }
}