package com.khurana.enterprise.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "sales")
@Data
public class SaleEntity {
	@Id
	@GeneratedValue
	private Long id;

	private String productName;
	private double price;
	private double mrp;
	private int quantity;
	private double totalAmount;
	private String shopName;
	private LocalDate saleDate;
}