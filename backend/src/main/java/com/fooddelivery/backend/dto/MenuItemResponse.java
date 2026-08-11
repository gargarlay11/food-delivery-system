package com.fooddelivery.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MenuItemResponse(

        Long id,

        Long restaurantId,
        String restaurantName,

        Long categoryId,
        String categoryName,

        String name,
        String description,

        BigDecimal price,

        String imageUrl,

        boolean available,

        Integer preparationTimeMinutes,

        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}