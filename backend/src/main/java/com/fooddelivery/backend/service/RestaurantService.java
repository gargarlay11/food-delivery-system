package com.fooddelivery.backend.service;

import com.fooddelivery.backend.dto.PageResponse;
import com.fooddelivery.backend.dto.RestaurantRequest;
import com.fooddelivery.backend.dto.RestaurantResponse;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantRequest request);

    RestaurantResponse getRestaurantById(Long id);

    PageResponse<RestaurantResponse> getAllRestaurants(
        String search,
        int page,
        int size,
        String sortBy,
        String sortDir
    );

    RestaurantResponse updateRestaurant(
        Long id,
        RestaurantRequest request
    );

    void deleteRestaurant(Long id);

}