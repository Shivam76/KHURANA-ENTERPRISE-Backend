package com.khurana.enterprise.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bill")
@Data
public class BillEntity {
	@Id
	@GeneratedValue
	private Long id;

	private String shopName;
	private String companyName;
	private double totalAmount;
	private boolean gstApplied;
	private String customerGstNumber;
	private String ownerGstNumber;
	private String ownerAddress;
	private LocalDateTime billDate;
	
	@ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
	@CollectionTable(name = "bill_products", joinColumns = @JoinColumn(name = "bill_id"))
	private List<ProductDetails> products;

}