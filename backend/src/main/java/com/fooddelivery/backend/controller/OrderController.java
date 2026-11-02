package com.fooddelivery.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.fooddelivery.backend.dto.CreateOrderRequest;
import com.fooddelivery.backend.dto.OrderResponse;
import com.fooddelivery.backend.service.OrderService;
import com.fooddelivery.backend.dto.UpdateOrderStatusRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            Authentication authentication) {

        String customerEmail = authentication.getName();

        OrderResponse response = orderService.createOrder(
                request, customerEmail);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            Authentication authentication) {

        String customerEmail = authentication.getName();

        return ResponseEntity.ok(
                orderService.getCustomerOrders(customerEmail));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponse>> getRestaurantOrders(
            @PathVariable Long restaurantId,
            Authentication authentication
    ) {

        String ownerEmail = authentication.getName();

        return ResponseEntity.ok(
                orderService.getRestaurantOrders(
                        restaurantId,
                        ownerEmail
                )
        );
    }

    @PatchMapping("/{orderId}/status")
        public ResponseEntity<OrderResponse> updateOrderStatus(
                @PathVariable Long orderId,
                @Valid @RequestBody UpdateOrderStatusRequest request,
                Authentication authentication
        ) {

        String ownerEmail = authentication.getName();

        OrderResponse response =
                orderService.updateOrderStatus(
                        orderId,
                        request.getStatus(),
                        ownerEmail
                );

        return ResponseEntity.ok(response);
        }
}