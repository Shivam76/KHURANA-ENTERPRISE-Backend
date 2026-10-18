package com.khurana.enterprise.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesSummaryDto {

	private String productName;
	private double price;
	private int totalQuantity;
	private double totalAmount;
	private String shopName;
	private LocalDate saleDate;
}
