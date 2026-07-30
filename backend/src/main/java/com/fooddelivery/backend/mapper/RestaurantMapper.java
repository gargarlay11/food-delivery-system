package com.fooddelivery.backend.mapper;

import com.fooddelivery.backend.dto.RestaurantRequest;
import com.fooddelivery.backend.dto.RestaurantResponse;
import com.fooddelivery.backend.entity.Restaurant;

public class RestaurantMapper {

    private RestaurantMapper() {
    }

    public static Restaurant toEntity(RestaurantRequest request) {

        Restaurant restaurant = new Restaurant();

        restaurant.setName(request.getName());
        restaurant.setEmail(request.getEmail());
        restaurant.setPhone(request.getPhone());
        restaurant.setAddress(request.getAddress());

        if (request.getActive() != null) {
            restaurant.setActive(request.getActive());
        }

        return restaurant;
    }

    public static RestaurantResponse toResponse(Restaurant restaurant) {

        return new RestaurantResponse(

                restaurant.getId(),
                restaurant.getName(),
                restaurant.getEmail(),
                restaurant.getPhone(),
                restaurant.getAddress(),
                restaurant.getActive(),
                restaurant.getCreatedAt(),
                restaurant.getUpdatedAt()

        );

    }

    public static void updateEntity(
        Restaurant restaurant,
        RestaurantRequest request
        ) { 
            restaurant.setName(request.getName());
            restaurant.setEmail(request.getEmail());
            restaurant.setPhone(request.getPhone());
            restaurant.setAddress(request.getAddress());

            if (request.getActive() != null) {
                restaurant.setActive(request.getActive());
            }
        }

}