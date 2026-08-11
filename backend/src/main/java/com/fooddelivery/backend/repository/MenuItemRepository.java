package com.fooddelivery.backend.repository;

import com.fooddelivery.backend.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantIdOrderByIdAsc(Long restaurantId);

    List<MenuItem> findByRestaurantIdAndCategoryIdOrderByIdAsc(
            Long restaurantId,
            Long categoryId
    );

    boolean existsByCategoryIdAndNameIgnoreCase(
            Long categoryId,
            String name
    );

    boolean existsByCategoryIdAndNameIgnoreCaseAndIdNot(
            Long categoryId,
            String name,
            Long id
    );
}