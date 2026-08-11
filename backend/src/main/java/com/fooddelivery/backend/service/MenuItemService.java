package com.fooddelivery.backend.service;

import com.fooddelivery.backend.dto.MenuItemRequest;
import com.fooddelivery.backend.dto.MenuItemResponse;

import java.util.List;

public interface MenuItemService {

    MenuItemResponse create(
            Long restaurantId,
            MenuItemRequest request
    );

    List<MenuItemResponse> getAllByRestaurant(
            Long restaurantId,
            Long categoryId
    );

    MenuItemResponse getById(
            Long restaurantId,
            Long menuItemId
    );

    MenuItemResponse update(
            Long restaurantId,
            Long menuItemId,
            MenuItemRequest request
    );

    void delete(
            Long restaurantId,
            Long menuItemId
    );
}