package com.fooddelivery.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse>
    handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors =
                new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                    errors.put(
                        error.getField(),
                        error.getDefaultMessage()
                    )
                );

        ApiErrorResponse response =
                new ApiErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Validation failed",
                    errors,
                    LocalDateTime.now()
                );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(
        RestaurantNotFoundException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleRestaurantNotFound(
            RestaurantNotFoundException exception
    ) {
        ApiErrorResponse response =
                new ApiErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    exception.getMessage(),
                    Map.of(),
                    LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(
        DuplicateRestaurantEmailException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleDuplicateEmail(
            DuplicateRestaurantEmailException exception
    ) {
        ApiErrorResponse response =
                new ApiErrorResponse(
                    HttpStatus.CONFLICT.value(),
                    exception.getMessage(),
                    Map.of(),
                    LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(
        MenuCategoryNotFoundException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleMenuCategoryNotFound(
            MenuCategoryNotFoundException exception
    ) {
        ApiErrorResponse response =
                new ApiErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    exception.getMessage(),
                    Map.of(),
                    LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(
        DuplicateMenuCategoryException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleDuplicateMenuCategory(
            DuplicateMenuCategoryException exception
    ) {
        ApiErrorResponse response =
                new ApiErrorResponse(
                    HttpStatus.CONFLICT.value(),
                    exception.getMessage(),
                    Map.of(),
                    LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(MenuItemNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleMenuItemNotFound(
            MenuItemNotFoundException exception
        ) {

        ApiErrorResponse response =
            new ApiErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    exception.getMessage(),
                    Map.of(),
                    LocalDateTime.now()
            );

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response);
        }

    @ExceptionHandler(DuplicateMenuItemException.class)
        public ResponseEntity<ApiErrorResponse> handleDuplicateMenuItem(
        DuplicateMenuItemException exception
        ) {

        ApiErrorResponse response =
            new ApiErrorResponse(
                    HttpStatus.CONFLICT.value(),
                    exception.getMessage(),
                    Map.of(),
                    LocalDateTime.now()
            );

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(response);
        }

    @ExceptionHandler(
        ImageStorageException.class
    )

    public ResponseEntity<ApiErrorResponse>
        handleImageStorageException(
            ImageStorageException exception
    ) {

        ApiErrorResponse response =
            new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                Map.of(),
                LocalDateTime.now()
            );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @ExceptionHandler(
        UserAlreadyExistsException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleUserAlreadyExists(
            UserAlreadyExistsException exception
    ) {

        ApiErrorResponse response =
                new ApiErrorResponse(
                    HttpStatus.CONFLICT.value(),
                    exception.getMessage(),
                    Map.of(),
                    LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(
        InvalidCredentialsException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleInvalidCredentials(
            InvalidCredentialsException exception
    ) {

        ApiErrorResponse response =
                new ApiErrorResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    exception.getMessage(),
                    Map.of(),
                    LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(
        RoleNotFoundException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleRoleNotFound(
            RoleNotFoundException exception
    ) {

        ApiErrorResponse response =
                new ApiErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage(),
                    Map.of(),
                    LocalDateTime.now()
                );

        return ResponseEntity
                .status(
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(response);
    }
    
}