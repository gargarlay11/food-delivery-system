package com.fooddelivery.backend.exception;

public class MenuCategoryNotFoundException
        extends RuntimeException {

    public MenuCategoryNotFoundException(Long id) {
        super("Menu category not found with id: " + id);
    }
}