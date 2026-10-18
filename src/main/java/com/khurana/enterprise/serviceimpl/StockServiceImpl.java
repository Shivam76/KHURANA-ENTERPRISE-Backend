package com.khurana.enterprise.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khurana.enterprise.dto.AddProductRequest;
import com.khurana.enterprise.entity.StockEntity;
import com.khurana.enterprise.exception.InvalidRequestException;
import com.khurana.enterprise.exception.ProductNotFoundException;
import com.khurana.enterprise.repository.StockRepository;
import com.khurana.enterprise.service.StockService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

	private final StockRepository repo;

	@Override
	public List<StockEntity> getAllStock() {
		log.info("Fetching all stock items");
		return repo.findAll();
	}

	@Override
	public List<StockEntity> getStockByName(String name) {
		log.info("Fetching stock for name: {}", name);
		return repo.findByName(name);
	}

	@Override
	public StockEntity getStockByNameAndPrice(String name, int price) {
		log.info("Fetching stock for name: {} and price: {}", name, price);
		StockEntity stock = repo.findByNameAndPrice(name, price);
		if (stock == null) {
			throw new ProductNotFoundException("Product not found: " + name + " at price " + price);
		}
		return stock;
	}

	@Override
	@Transactional
	public StockEntity addProduct(AddProductRequest request) {
		log.info("Adding product: {} price: {} box: {} pcsPerBox: {} loosePcs: {}", request.getName(), request.getPrice(), request.getBox(), request.getPcsPerBox(), request.getQuantity());

		// Total quantity = (boxes * pcsPerBox) + loose pieces (quantity)
		// Example: 2 boxes of 30 + 15 loose = 75 total pcs
		// Example: 0 boxes + 30 loose pcs = 30 total pcs
		int boxQuantity = (request.getBox() > 0 && request.getPcsPerBox() > 0)
				? request.getBox() * request.getPcsPerBox()
				: 0;
		int totalQuantity = boxQuantity + request.getQuantity();

		if (totalQuantity <= 0) {
			throw new InvalidRequestException("Either box or quantity must be provided");
		}

		StockEntity existingStock = repo.findByNameAndPrice(request.getName(), request.getPrice());
		if (existingStock != null) {
			existingStock.setQuantity(existingStock.getQuantity() + totalQuantity);
			if (request.getPcsPerBox() > 0) {
				existingStock.setPcsPerBox(request.getPcsPerBox());
			}
			recalculateBoxes(existingStock);
			return repo.save(existingStock);
		}

		StockEntity stock = new StockEntity();
		stock.setName(request.getName());
		stock.setPrice(request.getPrice());
		stock.setQuantity(totalQuantity);
		stock.setPcsPerBox(request.getPcsPerBox());
		stock.setHsnCode(request.getHsnCode());
		stock.setGstRate(request.getGstRate());
		recalculateBoxes(stock);
		return repo.save(stock);
	}

	@Override
	@Transactional
	public StockEntity updateStock(String name, int price, int quantity) {
		log.info("Updating stock for name: {} price: {} qty: {}", name, price, quantity);
		StockEntity stock = getStockByNameAndPrice(name, price);
		stock.setQuantity(stock.getQuantity() + quantity);
		recalculateBoxes(stock);
		return repo.save(stock);
	}

	private void recalculateBoxes(StockEntity stock) {
		if (stock.getPcsPerBox() > 0) {
			stock.setBox(stock.getQuantity() / stock.getPcsPerBox());
			stock.setLoosePcs(stock.getQuantity() % stock.getPcsPerBox());
		}
	}

	@Override
	@Transactional
	public StockEntity updateGstRate(String name, int price, double gstRate) {
		log.info("Updating GST rate for product: {} price: {} newGstRate: {}%", name, price, gstRate);
		StockEntity stock = getStockByNameAndPrice(name, price);
		stock.setGstRate(gstRate);
		return repo.save(stock);
	}
}
