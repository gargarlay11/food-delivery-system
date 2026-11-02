
package com.fooddelivery.backend.service;

import java.util.List;

import com.fooddelivery.backend.dto.CreateOrderRequest;
import com.fooddelivery.backend.dto.OrderResponse;
import com.fooddelivery.backend.entity.OrderStatus;

public interface OrderService {

    OrderResponse createOrder(
            CreateOrderRequest request,
            String customerEmail
    );

    List<OrderResponse> getCustomerOrders(
            String customerEmail
    );

    List<OrderResponse> getRestaurantOrders(
            Long restaurantId,
            String ownerEmail
    );
    
    OrderResponse updateOrderStatus(
        Long orderId,
        OrderStatus newStatus,
        String ownerEmail
    );
}

