package com.khurana.enterprise.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ProductDetails {

	private int serialNo;
	private String productName;
	private double price;
	private double mrp;
	private double grossAmount;
	private double gstRate;
	private double amount;
	private int quantity;
	private int hsnCode;
}
