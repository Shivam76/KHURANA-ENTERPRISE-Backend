package com.khurana.enterprise.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.khurana.enterprise.dto.BillCreationDto;
import com.khurana.enterprise.entity.BillEntity;
import com.khurana.enterprise.service.BillService;
import com.khurana.enterprise.service.BillingService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillingController {
	
	private final BillingService service;
	private final BillService billService;

	@Operation(summary = "Create bill", description = "Creates a new bill for the given shop with products and GST details. Set skipStockCheck=true to skip stock validation and deduction.")
	@PostMapping
	public BillEntity create(@RequestParam String shopName,
							 @Valid @RequestBody BillCreationDto request,
							 @RequestParam boolean gst,
							 @RequestParam(required = false) String customerGstNumber,
							 @RequestParam(required = false, defaultValue = "false") boolean skipStockCheck) {
		return service.createBill(shopName, request, gst, customerGstNumber, skipStockCheck);
	}

	@Operation(summary = "Get today's bills", description = "Fetches all bills generated today")
	@GetMapping("/today")
	public List<BillEntity> getTodayBills() {
		return billService.getTodayBills();
	}

	@Operation(summary = "Get yesterday's bills", description = "Fetches all bills generated yesterday")
	@GetMapping("/yesterday")
	public List<BillEntity> getYesterdayBills() {
		return billService.getYesterdayBills();
	}

	@Operation(summary = "Get last week's bills", description = "Fetches all bills from the last 7 days")
	@GetMapping("/week")
	public List<BillEntity> getLastWeekBills() {
		return billService.getLastWeekBills();
	}

	@Operation(summary = "Get last month's bills", description = "Fetches all bills from the last month")
	@GetMapping("/month")
	public List<BillEntity> getLastMonthBills() {
		return billService.getLastMonthBills();
	}

	@Operation(summary = "Get bills by specific month", description = "Fetches all bills for a specific month and year")
	@GetMapping("/month/{month}/year/{year}")
	public List<BillEntity> getBillsByMonth(@PathVariable int month, @PathVariable int year) {
		return billService.getBillsByMonth(month, year);
	}

	@Operation(summary = "Get bills by year", description = "Fetches all bills for a specific year")
	@GetMapping("/year/{year}")
	public List<BillEntity> getBillsByYear(@PathVariable int year) {
		return billService.getBillsByYear(year);
	}

	@Operation(summary = "Get bills by shop name", description = "Fetches all bills for a particular shop")
	@GetMapping("/shop/{shopName}")
	public List<BillEntity> getBillsByShopName(@PathVariable String shopName) {
		return billService.getBillsByShopName(shopName);
	}
}