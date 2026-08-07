package com.fooddelivery.backend.mapper;

import com.fooddelivery.backend.dto.MenuCategoryRequest;
import com.fooddelivery.backend.dto.MenuCategoryResponse;
import com.fooddelivery.backend.entity.MenuCategory;
import com.fooddelivery.backend.entity.Restaurant;

public class MenuCategoryMapper {

    private MenuCategoryMapper() {
    }

    public static MenuCategory toEntity(
            MenuCategoryRequest request,
            Restaurant restaurant
    ) {
        MenuCategory category =
                new MenuCategory();

        category.setRestaurant(restaurant);

        category.setName(
                request.getName().trim()
        );

        category.setDescription(
                normalizeDescription(
                        request.getDescription()
                )
        );

        if (request.getDisplayOrder() != null) {
            category.setDisplayOrder(
                    request.getDisplayOrder()
            );
        }

        if (request.getActive() != null) {
            category.setActive(
                    request.getActive()
            );
        }

        return category;
    }

    public static MenuCategoryResponse toResponse(
            MenuCategory category
    ) {
        return new MenuCategoryResponse(
                category.getId(),
                category.getRestaurant().getId(),
                category.getRestaurant().getName(),
                category.getName(),
                category.getDescription(),
                category.getDisplayOrder(),
                category.getActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public static void updateEntity(
            MenuCategory category,
            MenuCategoryRequest request
    ) {
        category.setName(
                request.getName().trim()
        );

        category.setDescription(
                normalizeDescription(
                        request.getDescription()
                )
        );

        if (request.getDisplayOrder() != null) {
            category.setDisplayOrder(
                    request.getDisplayOrder()
            );
        }

        if (request.getActive() != null) {
            category.setActive(
                    request.getActive()
            );
        }
    }

    private static String normalizeDescription(
            String description
    ) {
        if (
            description == null ||
            description.isBlank()
        ) {
            return null;
        }

        return description.trim();
    }
}