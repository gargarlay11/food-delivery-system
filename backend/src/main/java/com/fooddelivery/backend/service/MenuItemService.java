package com.fooddelivery.backend.service;

import com.fooddelivery.backend.dto.MenuItemRequest;
import com.fooddelivery.backend.dto.MenuItemResponse;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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

    MenuItemResponse uploadMenuItemImage(
        Long restaurantId,
        Long menuItemId,
        MultipartFile file
    );

    MenuItemResponse deleteMenuItemImage(
        Long restaurantId,
        Long menuItemId
    );
}