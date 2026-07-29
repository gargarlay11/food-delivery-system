package com.fooddelivery.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RestaurantRequest {

    @NotBlank(message = "Restaurant name is required")
    @Size(
        min = 2,
        max = 150,
        message = "Restaurant name must be between 2 and 150 characters"
    )
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Size(
        max = 100,
        message = "Email must not exceed 100 characters"
    )
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[+]?[0-9()\\-\\s]{7,20}$",
        message = "Phone number format is invalid"
    )
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(
        min = 5,
        max = 255,
        message = "Address must be between 5 and 255 characters"
    )
    private String address;

    private Boolean active;

    public RestaurantRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}