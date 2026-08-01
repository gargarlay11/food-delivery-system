package com.fooddelivery.backend.repository;

import com.fooddelivery.backend.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Restaurant> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );    
}