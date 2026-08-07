package com.fooddelivery.backend.service;

import java.util.List;

import com.fooddelivery.backend.dto.MenuCategoryRequest;
import com.fooddelivery.backend.dto.MenuCategoryResponse;

public interface MenuCategoryService {

    MenuCategoryResponse createCategory(
            Long restaurantId,
            MenuCategoryRequest request
    );

    MenuCategoryResponse getCategoryById(
            Long categoryId
    );

    List<MenuCategoryResponse> getCategoriesByRestaurant(
            Long restaurantId
    );

    MenuCategoryResponse updateCategory(
            Long categoryId,
            MenuCategoryRequest request
    );

    void deleteCategory(
            Long categoryId
    );
}