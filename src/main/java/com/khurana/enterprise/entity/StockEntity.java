package com.khurana.enterprise.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "stock")
@Data
public class StockEntity {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private double price;
	private int quantity;
	private int box;
	private int loosePcs;
	private int pcsPerBox;
	private int hsnCode;
	private double gstRate;
}