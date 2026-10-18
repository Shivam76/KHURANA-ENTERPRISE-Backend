package com.khurana.enterprise.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BillCreationDto {

	@NotNull(message = "Products list cannot be null")
	@NotEmpty(message = "Products list cannot be empty")
	@Valid
	private List<ProductBill> prodcuts;

	@Data
	public static class ProductBill {
		@NotBlank(message = "Product name cannot be blank")
		private String productName;

		@NotNull(message = "Price is required")
		@Min(value = 1, message = "Price must be greater than zero")
		private double price;

		@Min(value = 1, message = "MRP must be greater than zero")
		private double mrp;

		@Min(value = 1, message = "Quantity must be at least 1")
		private int qty;
	}
}
