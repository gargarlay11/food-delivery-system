package com.fooddelivery.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.backend.dto.MenuCategoryRequest;
import com.fooddelivery.backend.dto.MenuCategoryResponse;
import com.fooddelivery.backend.service.MenuCategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class MenuCategoryController {

    private final MenuCategoryService menuCategoryService;

    public MenuCategoryController(
            MenuCategoryService menuCategoryService
    ) {
        this.menuCategoryService =
                menuCategoryService;
    }

    @PostMapping(
        "/restaurants/{restaurantId}/categories"
    )
    public ResponseEntity<MenuCategoryResponse>
    createCategory(
            @PathVariable Long restaurantId,
            @Valid
            @RequestBody MenuCategoryRequest request
    ) {
        MenuCategoryResponse response =
                menuCategoryService.createCategory(
                    restaurantId,
                    request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(
        "/restaurants/{restaurantId}/categories"
    )
    public ResponseEntity<List<MenuCategoryResponse>>
    getCategoriesByRestaurant(
            @PathVariable Long restaurantId
    ) {
        List<MenuCategoryResponse> response =
                menuCategoryService
                    .getCategoriesByRestaurant(
                        restaurantId
                    );

        return ResponseEntity.ok(response);
    }

    @GetMapping(
        "/categories/{categoryId}"
    )
    public ResponseEntity<MenuCategoryResponse>
    getCategoryById(
            @PathVariable Long categoryId
    ) {
        MenuCategoryResponse response =
                menuCategoryService
                    .getCategoryById(
                        categoryId
                    );

        return ResponseEntity.ok(response);
    }

    @PutMapping(
        "/categories/{categoryId}"
    )
    public ResponseEntity<MenuCategoryResponse>
    updateCategory(
            @PathVariable Long categoryId,
            @Valid
            @RequestBody MenuCategoryRequest request
    ) {
        MenuCategoryResponse response =
                menuCategoryService
                    .updateCategory(
                        categoryId,
                        request
                    );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(
        "/categories/{categoryId}"
    )
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId
    ) {
        menuCategoryService.deleteCategory(
            categoryId
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}