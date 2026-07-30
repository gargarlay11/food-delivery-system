package com.fooddelivery.backend.controller;

import com.fooddelivery.backend.dto.RestaurantRequest;
import com.fooddelivery.backend.dto.RestaurantResponse;
import com.fooddelivery.backend.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(
            RestaurantService restaurantService
    ) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Valid @RequestBody RestaurantRequest request
    ) {
        RestaurantResponse response =
                restaurantService.createRestaurant(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                restaurantService.getRestaurantById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>>
    getAllRestaurants() {
        return ResponseEntity.ok(
                restaurantService.getAllRestaurants()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequest request
    ) {
        return ResponseEntity.ok(
                restaurantService.updateRestaurant(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable Long id
    ) {
        restaurantService.deleteRestaurant(id);

        return ResponseEntity.noContent().build();
    }
}