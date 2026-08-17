package com.fooddelivery.backend.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fooddelivery.backend.exception.ImageStorageException;
import com.fooddelivery.backend.service.ImageStorageService;

@Service
public class LocalImageStorageService
        implements ImageStorageService {

    private static final Set<String>
            ALLOWED_CONTENT_TYPES =
            Set.of(
                "image/jpeg",
                "image/png",
                "image/webp"
            );

    private final Path uploadDirectory;

    public LocalImageStorageService(
            @Value(
                "${app.upload.menu-items-dir}"
            )
            String uploadDirectory
    ) {

        this.uploadDirectory =
                Paths.get(uploadDirectory)
                    .toAbsolutePath()
                    .normalize();

        createUploadDirectory();
    }

    @Override
    public String storeMenuItemImage(
            MultipartFile file
    ) {

        validateFile(file);

        String originalFilename =
                file.getOriginalFilename();

        String extension =
                getFileExtension(
                    originalFilename
                );

        String newFilename =
                UUID.randomUUID()
                + extension;

        Path targetPath =
                uploadDirectory.resolve(
                    newFilename
                );

        try {

            Files.copy(
                file.getInputStream(),
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException exception) {

            throw new ImageStorageException(
                "Failed to store menu item image",
                exception
            );
        }

        return "/uploads/menu-items/"
                + newFilename;
    }

    @Override
    public void deleteMenuItemImage(
            String imageUrl
    ) {

        if (
            imageUrl == null
            || imageUrl.isBlank()
        ) {
            return;
        }

        String filename =
                Paths.get(imageUrl)
                    .getFileName()
                    .toString();

        Path filePath =
                uploadDirectory.resolve(
                    filename
                );

        try {

            Files.deleteIfExists(
                filePath
            );

        } catch (IOException exception) {

            throw new ImageStorageException(
                "Failed to delete menu item image",
                exception
            );
        }
    }

    private void validateFile(
            MultipartFile file
    ) {

        if (
            file == null
            || file.isEmpty()
        ) {
            throw new ImageStorageException(
                "Image file is required"
            );
        }

        String contentType =
                file.getContentType();

        if (
            contentType == null
            || !ALLOWED_CONTENT_TYPES
                .contains(contentType)
        ) {

            throw new ImageStorageException(
                "Only JPG, PNG and WEBP images are allowed"
            );
        }
    }

    private String getFileExtension(
            String filename
    ) {

        if (
            filename == null
            || !filename.contains(".")
        ) {
            return "";
        }

        return filename.substring(
            filename.lastIndexOf(".")
        );
    }

    private void createUploadDirectory() {

        try {

            Files.createDirectories(
                uploadDirectory
            );

        } catch (IOException exception) {

            throw new ImageStorageException(
                "Failed to create upload directory",
                exception
            );
        }
    }
}