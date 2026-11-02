
package com.fooddelivery.backend.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fooddelivery.backend.dto.CreateOrderRequest;
import com.fooddelivery.backend.dto.OrderItemRequest;
import com.fooddelivery.backend.dto.OrderItemResponse;
import com.fooddelivery.backend.dto.OrderResponse;
import com.fooddelivery.backend.entity.MenuItem;
import com.fooddelivery.backend.entity.Order;
import com.fooddelivery.backend.entity.OrderItem;
import com.fooddelivery.backend.entity.OrderStatus;
import com.fooddelivery.backend.entity.Restaurant;
import com.fooddelivery.backend.entity.User;
import com.fooddelivery.backend.repository.MenuItemRepository;
import com.fooddelivery.backend.repository.OrderItemRepository;
import com.fooddelivery.backend.repository.OrderRepository;
import com.fooddelivery.backend.repository.RestaurantRepository;
import com.fooddelivery.backend.repository.UserRepository;
import com.fooddelivery.backend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository,
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public OrderResponse createOrder(
            CreateOrderRequest request,
            String customerEmail
    ) {

        User customer = userRepository
                .findByEmailIgnoreCase(customerEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found"
                        )
                );

        Restaurant restaurant = restaurantRepository
                .findById(request.getRestaurantId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Restaurant not found"
                        )
                );

        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);

        order = orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            MenuItem menuItem = menuItemRepository
                    .findById(itemRequest.getMenuItemId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Menu item not found"
                            )
                    );

            if (!menuItem.getRestaurant().getId()
                    .equals(restaurant.getId())) {

                throw new IllegalArgumentException(
                        "Menu item does not belong to this restaurant"
                );
            }

            if (!menuItem.isAvailable()) {

                throw new IllegalArgumentException(
                        "Menu item is currently unavailable: "
                                + menuItem.getName()
                );
            }

            BigDecimal unitPrice = menuItem.getPrice();

            BigDecimal subtotal = unitPrice.multiply(
                    BigDecimal.valueOf(
                            itemRequest.getQuantity()
                    )
            );

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(unitPrice);
            orderItem.setSubtotal(subtotal);

            orderItemRepository.save(orderItem);

            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);

        order = orderRepository.save(order);

        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getCustomerOrders(
            String customerEmail
    ) {

        User customer = userRepository
                .findByEmailIgnoreCase(customerEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found"
                        )
                );

        return orderRepository
                .findByCustomer_IdOrderByCreatedAtDesc(
                        customer.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getRestaurantOrders(
            Long restaurantId,
            String ownerEmail
    ) {

        User owner = userRepository
                .findByEmailIgnoreCase(ownerEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );

        Restaurant restaurant = restaurantRepository
                .findByIdAndOwnerId(
                        restaurantId,
                        owner.getId()
                )
                .orElseThrow(() ->
                        new AccessDeniedException(
                                "You do not own this restaurant"
                        )
                );

        return orderRepository
                .findByRestaurant_IdOrderByCreatedAtDesc(
                        restaurant.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
        public OrderResponse updateOrderStatus(
                Long orderId,
                OrderStatus newStatus,
                String ownerEmail
        ) {

        // 1. Find the logged-in restaurant owner
        User owner = userRepository
                .findByEmailIgnoreCase(ownerEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );

        // 2. Find the order
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Order not found"
                        )
                );

        // 3. Verify restaurant ownership
        restaurantRepository
                .findByIdAndOwnerId(
                        order.getRestaurant().getId(),
                        owner.getId()
                )
                .orElseThrow(() ->
                        new AccessDeniedException(
                                "You do not own this restaurant"
                        )
                );

        // 4. Validate status transition
        OrderStatus currentStatus = order.getStatus();

        if (!isValidStatusTransition(
                currentStatus,
                newStatus
        )) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid order status transition: "
                                + currentStatus
                                + " -> "
                                + newStatus
                );
        }

        // 5. Update and save
        order.setStatus(newStatus);

        Order updatedOrder = orderRepository.save(order);

        return mapToResponse(updatedOrder);
        }


        // Validate allowed status transitions
        private boolean isValidStatusTransition(
                OrderStatus currentStatus,
                OrderStatus newStatus
        ) {

        return switch (currentStatus) {

                case PENDING ->
                        newStatus == OrderStatus.CONFIRMED;

                case CONFIRMED ->
                        newStatus == OrderStatus.PREPARING;

                case PREPARING ->
                        newStatus == OrderStatus.READY_FOR_PICKUP;

                case READY_FOR_PICKUP ->
                        newStatus == OrderStatus.OUT_FOR_DELIVERY;

                case OUT_FOR_DELIVERY ->
                        newStatus == OrderStatus.DELIVERED;

                case DELIVERED, CANCELLED -> false;
        };
        }

    private OrderResponse mapToResponse(Order order) {

        List<OrderItemResponse> items =
                orderItemRepository
                        .findByOrder_Id(order.getId())
                        .stream()
                        .map(item -> new OrderItemResponse(
                                item.getMenuItem().getId(),
                                item.getMenuItem().getName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getSubtotal()
                        ))
                        .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getRestaurant().getId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                items
        );
    }
}
