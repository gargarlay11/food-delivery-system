
package com.fooddelivery.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooddelivery.backend.entity.Order;


public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByCustomer_IdOrderByCreatedAtDesc(
            Long customerId
    );

    List<Order> findByRestaurant_IdOrderByCreatedAtDesc(
            Long restaurantId
    );
}
