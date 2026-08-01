package com.fooddelivery.backend.service.impl;

import com.fooddelivery.backend.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.fooddelivery.backend.dto.RestaurantRequest;
import com.fooddelivery.backend.dto.RestaurantResponse;
import com.fooddelivery.backend.entity.Restaurant;
import com.fooddelivery.backend.mapper.RestaurantMapper;
import com.fooddelivery.backend.repository.RestaurantRepository;
import com.fooddelivery.backend.service.RestaurantService;
import org.springframework.stereotype.Service;
import com.fooddelivery.backend.exception.DuplicateRestaurantEmailException;
import com.fooddelivery.backend.exception.RestaurantNotFoundException;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(
            RestaurantRepository restaurantRepository
    ) {
        this.restaurantRepository = restaurantRepository;
    }

        @Override
        public RestaurantResponse createRestaurant(RestaurantRequest request) {

                if (restaurantRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateRestaurantEmailException(request.getEmail());
                }

                Restaurant restaurant =
                        RestaurantMapper.toEntity(request);

                Restaurant savedRestaurant =
                        restaurantRepository.save(restaurant);

                return RestaurantMapper.toResponse(savedRestaurant);
        }

        @Override
        public RestaurantResponse getRestaurantById(Long id) {

                Restaurant restaurant =
                        restaurantRepository.findById(id)
                                .orElseThrow(() ->
                                        new RestaurantNotFoundException(id)
                                );

                return RestaurantMapper.toResponse(restaurant);
        }

        @Override
        public PageResponse<RestaurantResponse> getAllRestaurants(
                String search,
                int page,
                int size,
                String sortBy,
                String sortDir
        ) {
        Sort.Direction direction =
                sortDir.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable =
                PageRequest.of(page, size, sort);

        Page<Restaurant> restaurantPage;

        if (search == null || search.isBlank()) {
                restaurantPage =
                        restaurantRepository.findAll(pageable);
        } else {
                restaurantPage =
                        restaurantRepository
                                .findByNameContainingIgnoreCase(
                                        search.trim(),
                                        pageable
                                );
        }

        var content = restaurantPage
                .getContent()
                .stream()
                .map(RestaurantMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                restaurantPage.getNumber(),
                restaurantPage.getSize(),
                restaurantPage.getTotalElements(),
                restaurantPage.getTotalPages(),
                restaurantPage.isFirst(),
                restaurantPage.isLast()
        );
        }

        @Override
        public RestaurantResponse updateRestaurant(
                Long id,
                RestaurantRequest request
        ) {
                Restaurant restaurant =
                        restaurantRepository.findById(id)
                                .orElseThrow(() ->
                                        new RestaurantNotFoundException(id)
                                );

                boolean emailChanged =
                        !restaurant.getEmail().equalsIgnoreCase(
                                request.getEmail()
                        );

                if (emailChanged &&
                        restaurantRepository.existsByEmail(
                                request.getEmail()
                        )) {
                throw new DuplicateRestaurantEmailException(
                        request.getEmail()
                );
                }

                RestaurantMapper.updateEntity(
                        restaurant,
                        request
                );

                Restaurant updatedRestaurant =
                        restaurantRepository.save(restaurant);

                return RestaurantMapper.toResponse(updatedRestaurant);
        } 
        
        @Override
        public void deleteRestaurant(Long id) {
                Restaurant restaurant =
                        restaurantRepository.findById(id)
                                .orElseThrow(() ->
                                        new RestaurantNotFoundException(id)
                                );

                restaurantRepository.delete(restaurant);
        }    

    
}