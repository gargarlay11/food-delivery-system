package com.fooddelivery.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooddelivery.backend.entity.MenuCategory;

public interface MenuCategoryRepository
        extends JpaRepository<MenuCategory, Long> {

    List<MenuCategory>
    findByRestaurantIdOrderByDisplayOrderAscNameAsc(
            Long restaurantId
    );

    boolean existsByRestaurantIdAndNameIgnoreCase(
            Long restaurantId,
            String name
    );

    boolean
    existsByRestaurantIdAndNameIgnoreCaseAndIdNot(
            Long restaurantId,
            String name,
            Long categoryId
    );
}