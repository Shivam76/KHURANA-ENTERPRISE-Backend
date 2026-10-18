package com.khurana.enterprise.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khurana.enterprise.dto.AddProductRequest;
import com.khurana.enterprise.entity.StockEntity;
import com.khurana.enterprise.service.StockService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
@Slf4j
public class StockController {
	private final StockService service;

	@Operation(summary = "Get all stock", description = "Retrieves all products from stock")
	@GetMapping
	public List<StockEntity> getAll() {
		return service.getAllStock();
	}

	@Operation(summary = "Get stock by name", description = "Retrieves all products from stock filtered by name")
	@GetMapping("/{name}")
	public List<StockEntity> byName(@PathVariable String name) {
		return service.getStockByName(name);
	}

	@Operation(summary = "Get stock by name and price", description = "Retrieves a product from stock by name and price")
	@GetMapping("/{name}/{price}")
	public StockEntity byNamePrice(@PathVariable String name, @PathVariable int price) {
		return service.getStockByNameAndPrice(name, price);
	}

	@Operation(summary = "Update stock quantity", description = "Updates the quantity of a product in stock by name and price")
	@PutMapping("/{name}/{price}/{qty}")
	public StockEntity update(@PathVariable String name, @PathVariable int price, @PathVariable int qty) {
		return service.updateStock(name, price, qty);
	}

	@Operation(summary = "Add product to stock", description = "Adds a biscuit product with name, price and quantity")
	@PostMapping("/add")
	public ResponseEntity<StockEntity> addProduct(@Valid @RequestBody AddProductRequest request) {

		log.info("Adding product: {} price: {}", request.getName(), request.getPrice());

		StockEntity stock = service.addProduct(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(stock);
	}

	@Operation(summary = "Update GST rate", description = "Updates the GST rate of a product by name and price")
	@PutMapping("/gst/{name}/{price}/{gstRate}")
	public StockEntity updateGstRate(@PathVariable String name, @PathVariable int price, @PathVariable double gstRate) {
		return service.updateGstRate(name, price, gstRate);
	}
}