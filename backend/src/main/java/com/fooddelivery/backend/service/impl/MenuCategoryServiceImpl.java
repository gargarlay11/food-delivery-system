package com.fooddelivery.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fooddelivery.backend.dto.MenuCategoryRequest;
import com.fooddelivery.backend.dto.MenuCategoryResponse;
import com.fooddelivery.backend.entity.MenuCategory;
import com.fooddelivery.backend.entity.Restaurant;
import com.fooddelivery.backend.exception.DuplicateMenuCategoryException;
import com.fooddelivery.backend.exception.MenuCategoryNotFoundException;
import com.fooddelivery.backend.exception.RestaurantNotFoundException;
import com.fooddelivery.backend.mapper.MenuCategoryMapper;
import com.fooddelivery.backend.repository.MenuCategoryRepository;
import com.fooddelivery.backend.repository.RestaurantRepository;
import com.fooddelivery.backend.service.MenuCategoryService;

@Service
@Transactional
public class MenuCategoryServiceImpl
        implements MenuCategoryService {

    private final MenuCategoryRepository menuCategoryRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuCategoryServiceImpl(
            MenuCategoryRepository menuCategoryRepository,
            RestaurantRepository restaurantRepository
    ) {
        this.menuCategoryRepository = menuCategoryRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public MenuCategoryResponse createCategory(
            Long restaurantId,
            MenuCategoryRequest request
    ) {
        Restaurant restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(
                    () -> new RestaurantNotFoundException(
                        restaurantId
                    )
                );

        String categoryName = request
                .getName()
                .trim();

        boolean categoryExists = menuCategoryRepository
                .existsByRestaurantIdAndNameIgnoreCase(
                    restaurantId,
                    categoryName
                );

        if (categoryExists) {
            throw new DuplicateMenuCategoryException(
                restaurantId,
                categoryName
            );
        }

        MenuCategory menuCategory =
                MenuCategoryMapper.toEntity(
                    request,
                    restaurant
                );

        MenuCategory savedCategory =
                menuCategoryRepository.save(
                    menuCategory
                );

        return MenuCategoryMapper.toResponse(
            savedCategory
        );
    }

    @Override
    @Transactional(readOnly = true)
    public MenuCategoryResponse getCategoryById(
            Long categoryId
    ) {
        MenuCategory menuCategory =
                findCategoryById(categoryId);

        return MenuCategoryMapper.toResponse(
            menuCategory
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuCategoryResponse>
    getCategoriesByRestaurant(
            Long restaurantId
    ) {
        boolean restaurantExists =
                restaurantRepository.existsById(
                    restaurantId
                );

        if (!restaurantExists) {
            throw new RestaurantNotFoundException(
                restaurantId
            );
        }

        return menuCategoryRepository
                .findByRestaurantIdOrderByDisplayOrderAscNameAsc(
                    restaurantId
                )
                .stream()
                .map(MenuCategoryMapper::toResponse)
                .toList();
    }

    @Override
    public MenuCategoryResponse updateCategory(
            Long categoryId,
            MenuCategoryRequest request
    ) {
        MenuCategory menuCategory =
                findCategoryById(categoryId);

        Long restaurantId = menuCategory
                .getRestaurant()
                .getId();

        String categoryName = request
                .getName()
                .trim();

        boolean duplicateExists = menuCategoryRepository
                .existsByRestaurantIdAndNameIgnoreCaseAndIdNot(
                    restaurantId,
                    categoryName,
                    categoryId
                );

        if (duplicateExists) {
            throw new DuplicateMenuCategoryException(
                restaurantId,
                categoryName
            );
        }

        MenuCategoryMapper.updateEntity(
            menuCategory,
            request
        );

        MenuCategory updatedCategory =
                menuCategoryRepository.save(
                    menuCategory
                );

        return MenuCategoryMapper.toResponse(
            updatedCategory
        );
    }

    @Override
    public void deleteCategory(
            Long categoryId
    ) {
        MenuCategory menuCategory =
                findCategoryById(categoryId);

        menuCategoryRepository.delete(
            menuCategory
        );
    }

    private MenuCategory findCategoryById(
            Long categoryId
    ) {
        return menuCategoryRepository
                .findById(categoryId)
                .orElseThrow(
                    () -> new MenuCategoryNotFoundException(
                        categoryId
                    )
                );
    }
}