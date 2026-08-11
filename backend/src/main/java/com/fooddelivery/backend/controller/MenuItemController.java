package com.fooddelivery.backend.controller;

import com.fooddelivery.backend.dto.MenuItemRequest;
import com.fooddelivery.backend.dto.MenuItemResponse;
import com.fooddelivery.backend.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(
            MenuItemService menuItemService
    ) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    public ResponseEntity<MenuItemResponse> create(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuItemRequest request
    ) {

        MenuItemResponse response =
                menuItemService.create(
                        restaurantId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> getAll(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) Long categoryId
    ) {

        return ResponseEntity.ok(
                menuItemService.getAllByRestaurant(
                        restaurantId,
                        categoryId
                )
        );
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponse> getById(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId
    ) {

        return ResponseEntity.ok(
                menuItemService.getById(
                        restaurantId,
                        menuItemId
                )
        );
    }

    @PutMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponse> update(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId,
            @Valid @RequestBody MenuItemRequest request
    ) {

        return ResponseEntity.ok(
                menuItemService.update(
                        restaurantId,
                        menuItemId,
                        request
                )
        );
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId
    ) {

        menuItemService.delete(
                restaurantId,
                menuItemId
        );

        return ResponseEntity.noContent().build();
    }
}