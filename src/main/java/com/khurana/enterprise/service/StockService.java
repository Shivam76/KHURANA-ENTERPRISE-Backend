package com.khurana.enterprise.service;

import java.util.List;

import com.khurana.enterprise.dto.AddProductRequest;
import com.khurana.enterprise.entity.StockEntity;

public interface StockService {

	List<StockEntity> getAllStock();

	List<StockEntity> getStockByName(String name);

	StockEntity getStockByNameAndPrice(String name, int price);

	StockEntity addProduct(AddProductRequest request);

	StockEntity updateStock(String name, int price, int quantity);

	StockEntity updateGstRate(String name, int price, double gstRate);
}
