package com.fooddelivery.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String storeMenuItemImage(
            MultipartFile file
    );

    void deleteMenuItemImage(
            String imageUrl
    );
}