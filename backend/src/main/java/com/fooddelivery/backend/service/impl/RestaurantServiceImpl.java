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
import com.fooddelivery.backend.repository.UserRepository;
import com.fooddelivery.backend.service.RestaurantService;
import org.springframework.stereotype.Service;
import com.fooddelivery.backend.exception.DuplicateRestaurantEmailException;
import com.fooddelivery.backend.exception.RestaurantNotFoundException;

import com.fooddelivery.backend.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    public RestaurantServiceImpl(
            RestaurantRepository restaurantRepository,
            UserRepository userRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

        @Override
        public RestaurantResponse createRestaurant(
                RestaurantRequest request
        ) {

        if (
                restaurantRepository.existsByEmail(
                        request.getEmail()
                )
        ) {
                throw new DuplicateRestaurantEmailException(
                        request.getEmail()
                );
        }

        // 1. JWT က authenticate လုပ်ထားတဲ့ user ကိုယူမယ်
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // 2. Authentication ထဲက login email ကိုယူမယ်
        String email =
                authentication.getName();

        // 3. အဲဒီ email နဲ့ User ကို database ထဲရှာမယ်
        User owner =
                userRepository
                        .findByEmailIgnoreCase(email)
                        .orElseThrow(
                                () -> new IllegalStateException(
                                        "Authenticated user not found"
                                )
                        );

        // 4. Request က Restaurant data ကို Entity ပြောင်းမယ်
        Restaurant restaurant =
                RestaurantMapper.toEntity(request);

        // 5. Login ဝင်ထားတဲ့ user ကို restaurant owner အဖြစ်ချိတ်မယ်
        restaurant.setOwner(owner);

        // 6. Restaurant + owner_user_id ကို database ထဲ save မယ်
        Restaurant savedRestaurant =
                restaurantRepository.save(
                        restaurant
                );

        return RestaurantMapper.toResponse(
                savedRestaurant
        );
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
                User currentUser = getCurrentUser();

                Restaurant restaurant =
                        restaurantRepository.findByIdAndOwnerId(
                                id,
                                currentUser.getId()
                )
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
                User currentUser = getCurrentUser();

                Restaurant restaurant =
                        restaurantRepository.findByIdAndOwnerId(
                                id,
                                currentUser.getId()
                        )
                .orElseThrow(() ->
                        new RestaurantNotFoundException(id)
                );

                restaurantRepository.delete(restaurant);
        }    

        private User getCurrentUser() {

         Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

        String email =
            authentication.getName();

         return userRepository
            .findByEmailIgnoreCase(email)
            .orElseThrow(
                    () -> new IllegalStateException(
                            "Authenticated user not found"
                    )
            );
        }
    
}