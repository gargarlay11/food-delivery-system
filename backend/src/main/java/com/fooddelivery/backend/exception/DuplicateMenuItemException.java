package com.fooddelivery.backend.exception;

public class DuplicateMenuItemException extends RuntimeException {

    public DuplicateMenuItemException(String name) {
        super("Menu item already exists with name: " + name);
    }
}