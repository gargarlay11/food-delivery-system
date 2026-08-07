package com.fooddelivery.backend.exception;

public class DuplicateMenuCategoryException
        extends RuntimeException {

    public DuplicateMenuCategoryException(
            Long restaurantId,
            String categoryName
    ) {
        super(
            "Menu category already exists in restaurant "
            + restaurantId
            + " with name: "
            + categoryName
        );
    }
}