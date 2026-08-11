package com.fooddelivery.backend.mapper;

import com.fooddelivery.backend.dto.MenuItemResponse;
import com.fooddelivery.backend.entity.MenuItem;

public final class MenuItemMapper {

    private MenuItemMapper() {
    }

    public static MenuItemResponse toResponse(MenuItem menuItem) {

        return new MenuItemResponse(
                menuItem.getId(),

                menuItem.getRestaurant().getId(),
                menuItem.getRestaurant().getName(),

                menuItem.getCategory().getId(),
                menuItem.getCategory().getName(),

                menuItem.getName(),
                menuItem.getDescription(),

                menuItem.getPrice(),

                menuItem.getImageUrl(),

                menuItem.isAvailable(),

                menuItem.getPreparationTimeMinutes(),

                menuItem.getCreatedAt(),
                menuItem.getUpdatedAt()
        );
    }
}