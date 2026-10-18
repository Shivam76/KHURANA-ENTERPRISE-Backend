package com.khurana.enterprise.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khurana.enterprise.dto.BillCreationDto;
import com.khurana.enterprise.dto.BillCreationDto.ProductBill;
import com.khurana.enterprise.entity.BillEntity;
import com.khurana.enterprise.entity.ProductDetails;
import com.khurana.enterprise.entity.SaleEntity;
import com.khurana.enterprise.entity.StockEntity;
import com.khurana.enterprise.exception.InsufficientStockException;
import com.khurana.enterprise.exception.InvalidRequestException;
import com.khurana.enterprise.exception.ProductNotFoundException;
import com.khurana.enterprise.repository.BillRepository;
import com.khurana.enterprise.repository.SaleRepository;
import com.khurana.enterprise.repository.StockRepository;
import com.khurana.enterprise.service.BillingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

	private final StockRepository stockRepo;
	private final BillRepository billRepo;
	private final SaleRepository saleRepo;

	@Override
	@Transactional
	public BillEntity createBill(String shop, BillCreationDto request, boolean gst, String customerGstNumber, boolean skipStockCheck) {

		log.info("Creating bill for shop: {} | skipStockCheck: {}", shop, skipStockCheck);
		validateRequest(request);

		double totalAmount = 0;
		int serialNo = 1;
		List<ProductDetails> productList = new ArrayList<>();
		List<StockEntity> stocksToUpdate = new ArrayList<>();
		List<SaleEntity> salesToSave = new ArrayList<>();

		for (ProductBill product : request.getProdcuts()) {
			StockEntity stock = fetchStock(product);

			if (!skipStockCheck) {
				deductStock(stock, product);
				stocksToUpdate.add(stock);
			}

			double grossAmount = product.getPrice() * product.getQty();
			double amount = calculateAmount(grossAmount, stock, gst);
			salesToSave.add(buildSaleEntity(product, shop, amount));
			productList.add(buildProductDetails(product, stock, grossAmount, amount, serialNo++));

			log.info("Current total amount for bill: {}", totalAmount);
			totalAmount += amount;
		}

		stockRepo.saveAll(stocksToUpdate);
		saleRepo.saveAll(salesToSave);

		log.info("Final total amount for bill: {}", totalAmount);
		return saveBill(shop, gst, customerGstNumber, totalAmount, productList);
	}

	private void validateRequest(BillCreationDto request) {
		if (request == null || request.getProdcuts() == null || request.getProdcuts().isEmpty()) {
			throw new InvalidRequestException("Bill must contain at least one product");
		}
	}

	private StockEntity fetchStock(ProductBill product) {
		StockEntity stock = stockRepo.findByNameAndPrice(product.getProductName(), product.getMrp());
		if (stock == null) {
			throw new ProductNotFoundException("Product not found: " + product.getProductName() + " at price " + product.getPrice());
		}
		return stock;
	}

	private void deductStock(StockEntity stock, ProductBill product) {
		log.info("Checking stock for product: {} | Requested: {} | Available: {}", product.getProductName(), product.getQty(), stock.getQuantity());

		if (stock.getQuantity() < product.getQty()) {
			throw new InsufficientStockException("Insufficient stock for '" + product.getProductName() + "'. Available: " + stock.getQuantity() + ", Requested: " + product.getQty());
		}

		log.info("Deducting stock for product: {} | Quantity to Deduct: {}", product.getProductName(), product.getQty());
		stock.setQuantity(stock.getQuantity() - product.getQty());
		recalculateBoxes(stock);
	}

	private void recalculateBoxes(StockEntity stock) {
		if (stock.getPcsPerBox() <= 0) {
			return;
		}
		log.info("Calculating box and loose pieces for product: {} | Remaining: {} | PcsPerBox: {}", stock.getName(), stock.getQuantity(), stock.getPcsPerBox());
		stock.setBox(stock.getQuantity() / stock.getPcsPerBox());
		stock.setLoosePcs(stock.getQuantity() % stock.getPcsPerBox());
		log.info("Updated stock: {} | Box: {} | Loose Pieces: {}", stock.getName(), stock.getBox(), stock.getLoosePcs());
	}

	private double calculateAmount(double grossAmount, StockEntity stock, boolean gst) {
		double amount = grossAmount;
		if (gst && stock.getGstRate() > 0) {
			log.info("Applying GST for product: {} | Rate: {}% | Gross Amount: {}", stock.getName(), stock.getGstRate(), grossAmount);
			amount += amount * (stock.getGstRate() / 100);
			log.info("Amount after GST: {}", amount);
		}
		return amount;
	}

	private SaleEntity buildSaleEntity(ProductBill product, String shopName, double amount) {
		SaleEntity sale = new SaleEntity();
		sale.setProductName(product.getProductName());
		sale.setPrice(product.getPrice());
		sale.setMrp(product.getMrp());
		sale.setQuantity(product.getQty());
		sale.setTotalAmount(amount);
		sale.setShopName(shopName);
		sale.setSaleDate(LocalDate.now());
		return sale;
	}

	private ProductDetails buildProductDetails(ProductBill product, StockEntity stock, double grossAmount, double amount, int serialNo) {
		log.info("Adding product to bill: {} | Quantity: {} | GrossAmount: {} | Amount: {}", product.getProductName(), product.getQty(), grossAmount, amount);
		ProductDetails productDetails = new ProductDetails();
		productDetails.setSerialNo(serialNo);
		productDetails.setProductName(product.getProductName());
		productDetails.setPrice(product.getPrice());
		productDetails.setMrp(product.getMrp());
		productDetails.setGrossAmount(grossAmount);
		productDetails.setGstRate(stock.getGstRate());
		productDetails.setAmount(amount);
		productDetails.setQuantity(product.getQty());
		productDetails.setHsnCode(stock.getHsnCode());
		return productDetails;
	}

	private BillEntity saveBill(String shop, boolean gst, String customerGstNumber, double totalAmount, List<ProductDetails> productList) {
		BillEntity bill = new BillEntity();
		bill.setProducts(productList);
		bill.setShopName(shop);
		bill.setTotalAmount(totalAmount);
		bill.setCompanyName("Khurana Enterprise");
		bill.setCustomerGstNumber(customerGstNumber);
		bill.setOwnerAddress("Naveen Nagar");
		bill.setOwnerGstNumber("89349394");
		bill.setGstApplied(gst);
		bill.setBillDate(LocalDateTime.now());

		log.info("Saving bill for shop: {} | Total Amount: {}", shop, totalAmount);
		return billRepo.save(bill);
	}
}
