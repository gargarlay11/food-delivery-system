package com.fooddelivery.backend.service.impl;

import com.fooddelivery.backend.dto.MenuItemRequest;
import com.fooddelivery.backend.dto.MenuItemResponse;
import com.fooddelivery.backend.entity.MenuCategory;
import com.fooddelivery.backend.entity.MenuItem;
import com.fooddelivery.backend.entity.Restaurant;
import com.fooddelivery.backend.exception.DuplicateMenuItemException;
import com.fooddelivery.backend.exception.MenuCategoryNotFoundException;
import com.fooddelivery.backend.exception.MenuItemNotFoundException;
import com.fooddelivery.backend.exception.RestaurantNotFoundException;
import com.fooddelivery.backend.mapper.MenuItemMapper;
import com.fooddelivery.backend.repository.MenuCategoryRepository;
import com.fooddelivery.backend.repository.MenuItemRepository;
import com.fooddelivery.backend.repository.RestaurantRepository;
import com.fooddelivery.backend.service.MenuItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;
import com.fooddelivery.backend.service.ImageStorageService;

import java.util.List;

@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    private final ImageStorageService imageStorageService;

    public MenuItemServiceImpl(
            MenuItemRepository menuItemRepository,
            RestaurantRepository restaurantRepository,
            MenuCategoryRepository menuCategoryRepository,
            ImageStorageService imageStorageService
    ) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuCategoryRepository = menuCategoryRepository;
        this.imageStorageService = imageStorageService;
    }

    @Override
    public MenuItemResponse create(
            Long restaurantId,
            MenuItemRequest request
    ) {

        Restaurant restaurant = getRestaurant(restaurantId);

        MenuCategory category =
                getCategory(request.categoryId());

        validateCategoryBelongsToRestaurant(
                category,
                restaurantId
        );

        if (menuItemRepository
                .existsByCategoryIdAndNameIgnoreCase(
                        category.getId(),
                        request.name().trim()
                )) {

            throw new DuplicateMenuItemException(
                    request.name()
            );
        }

        boolean available =
                request.available() == null
                        || request.available();

        int preparationTime =
                request.preparationTimeMinutes() == null
                        ? 15
                        : request.preparationTimeMinutes();

        MenuItem menuItem =
                new MenuItem(
                        restaurant,
                        category,
                        request.name().trim(),
                        normalize(request.description()),
                        request.price(),
                        normalize(request.imageUrl()),
                        available,
                        preparationTime
                );

        MenuItem saved =
                menuItemRepository.save(menuItem);

        return MenuItemMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getAllByRestaurant(
            Long restaurantId,
            Long categoryId
    ) {

        getRestaurant(restaurantId);

        List<MenuItem> menuItems;

        if (categoryId == null) {

            menuItems =
                    menuItemRepository
                            .findByRestaurantIdOrderByIdAsc(
                                    restaurantId
                            );

        } else {

            MenuCategory category =
                    getCategory(categoryId);

            validateCategoryBelongsToRestaurant(
                    category,
                    restaurantId
            );

            menuItems =
                    menuItemRepository
                            .findByRestaurantIdAndCategoryIdOrderByIdAsc(
                                    restaurantId,
                                    categoryId
                            );
        }

        return menuItems.stream()
                .map(MenuItemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse getById(
            Long restaurantId,
            Long menuItemId
    ) {

        getRestaurant(restaurantId);

        MenuItem menuItem =
                getMenuItem(menuItemId);

        validateMenuItemBelongsToRestaurant(
                menuItem,
                restaurantId
        );

        return MenuItemMapper.toResponse(menuItem);
    }

    @Override
    public MenuItemResponse update(
            Long restaurantId,
            Long menuItemId,
            MenuItemRequest request
    ) {

        getRestaurant(restaurantId);

        MenuItem menuItem =
                getMenuItem(menuItemId);

        validateMenuItemBelongsToRestaurant(
                menuItem,
                restaurantId
        );

        MenuCategory category =
                getCategory(request.categoryId());

        validateCategoryBelongsToRestaurant(
                category,
                restaurantId
        );

        if (menuItemRepository
                .existsByCategoryIdAndNameIgnoreCaseAndIdNot(
                        category.getId(),
                        request.name().trim(),
                        menuItemId
                )) {

            throw new DuplicateMenuItemException(
                    request.name()
            );
        }

        menuItem.setCategory(category);
        menuItem.setName(request.name().trim());
        menuItem.setDescription(
                normalize(request.description())
        );
        menuItem.setPrice(request.price());
        menuItem.setImageUrl(
                normalize(request.imageUrl())
        );

        if (request.available() != null) {
            menuItem.setAvailable(
                    request.available()
            );
        }

        if (request.preparationTimeMinutes() != null) {
            menuItem.setPreparationTimeMinutes(
                    request.preparationTimeMinutes()
            );
        }

        MenuItem saved =
                menuItemRepository.save(menuItem);

        return MenuItemMapper.toResponse(saved);
    }

    @Override
    public void delete(
            Long restaurantId,
            Long menuItemId
    ) {

        getRestaurant(restaurantId);

        MenuItem menuItem =
                getMenuItem(menuItemId);

        validateMenuItemBelongsToRestaurant(
                menuItem,
                restaurantId
        );

        menuItemRepository.delete(menuItem);
    }

    private Restaurant getRestaurant(Long restaurantId) {

        return restaurantRepository
                .findById(restaurantId)
                .orElseThrow(
                        () -> new RestaurantNotFoundException(
                                restaurantId
                        )
                );
    }

    private MenuCategory getCategory(Long categoryId) {

        return menuCategoryRepository
                .findById(categoryId)
                .orElseThrow(
                        () -> new MenuCategoryNotFoundException(
                                categoryId
                        )
                );
    }

    private MenuItem getMenuItem(Long menuItemId) {

        return menuItemRepository
                .findById(menuItemId)
                .orElseThrow(
                        () -> new MenuItemNotFoundException(
                                menuItemId
                        )
                );
    }

    private void validateCategoryBelongsToRestaurant(
            MenuCategory category,
            Long restaurantId
    ) {

        if (!category.getRestaurant()
                .getId()
                .equals(restaurantId)) {

            throw new MenuCategoryNotFoundException(
                    category.getId()
            );
        }
    }

    private void validateMenuItemBelongsToRestaurant(
            MenuItem menuItem,
            Long restaurantId
    ) {

        if (!menuItem.getRestaurant()
                .getId()
                .equals(restaurantId)) {

            throw new MenuItemNotFoundException(
                    menuItem.getId()
            );
        }
    }

    private String normalize(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

        @Override
        public MenuItemResponse uploadMenuItemImage(
                Long restaurantId,
                Long menuItemId,
                MultipartFile file
        ) {

                getRestaurant(restaurantId);

                MenuItem menuItem =
                        getMenuItem(menuItemId);

                validateMenuItemBelongsToRestaurant(
                        menuItem,
                        restaurantId
                );

        String oldImageUrl =
            menuItem.getImageUrl();

        String newImageUrl =
            imageStorageService
                    .storeMenuItemImage(file);

        menuItem.setImageUrl(
            newImageUrl
        );

         MenuItem updatedMenuItem =
            menuItemRepository.save(
                    menuItem
            );

        if (
                oldImageUrl != null
                && !oldImageUrl.isBlank()
        ) {
                imageStorageService
                        .deleteMenuItemImage(
                                oldImageUrl
                        );
        }

                return MenuItemMapper.toResponse(
                        updatedMenuItem
                );
        }
        
        @Override
        public MenuItemResponse deleteMenuItemImage(
                Long restaurantId,
                Long menuItemId
        ) {

                getRestaurant(restaurantId);

                MenuItem menuItem =
                        getMenuItem(menuItemId);

                validateMenuItemBelongsToRestaurant(
                        menuItem,
                        restaurantId
                );

                String imageUrl =
                        menuItem.getImageUrl();

                if (
                        imageUrl != null
                        && !imageUrl.isBlank()
                ) {

                        imageStorageService
                                .deleteMenuItemImage(
                                        imageUrl
                                );

                        menuItem.setImageUrl(null);
                }

                MenuItem updatedMenuItem =
                        menuItemRepository.save(
                                menuItem
                        );

                return MenuItemMapper.toResponse(
                        updatedMenuItem
                );
        }

}