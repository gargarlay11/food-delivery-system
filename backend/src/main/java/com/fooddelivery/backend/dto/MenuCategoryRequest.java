package com.fooddelivery.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MenuCategoryRequest {

    @NotBlank(
        message = "Category name is required"
    )
    @Size(
        min = 2,
        max = 100,
        message =
            "Category name must be between 2 and 100 characters"
    )
    private String name;

    @Size(
        max = 255,
        message =
            "Description must not exceed 255 characters"
    )
    private String description;

    @Min(
        value = 0,
        message =
            "Display order must be zero or greater"
    )
    private Integer displayOrder;

    private Boolean active;

    public MenuCategoryRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description
    ) {
        this.description = description;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(
            Integer displayOrder
    ) {
        this.displayOrder = displayOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(
            Boolean active
    ) {
        this.active = active;
    }
}