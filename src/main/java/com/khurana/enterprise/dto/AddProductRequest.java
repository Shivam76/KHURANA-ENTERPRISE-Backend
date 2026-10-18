package com.khurana.enterprise.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddProductRequest {

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than zero")
    private int price;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;

    @Min(value = 0, message = "Box cannot be negative")
    private int box;

    @Min(value = 0, message = "Pieces per box cannot be negative")
    private int pcsPerBox;

    private int hsnCode;

    private double gstRate;
}
