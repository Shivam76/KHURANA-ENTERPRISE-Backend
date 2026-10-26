package com.khurana.enterprise.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khurana.enterprise.dto.SalesSummaryDto;
import com.khurana.enterprise.entity.SaleEntity;
import com.khurana.enterprise.service.SalesService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

	private final SalesService salesService;

	@Operation(summary = "Get daily sales", description = "Retrieves all sales for today")
	@GetMapping("/day")
	public List<SaleEntity> day() {
		return salesService.getDailySales();
	}

	@Operation(summary = "Get weekly sales", description = "Retrieves all sales from the last 7 days")
	@GetMapping("/week")
	public List<SaleEntity> week() {
		return salesService.getWeeklySales();
	}

	@Operation(summary = "Get monthly sales", description = "Retrieves all sales from the last month")
	@GetMapping("/month")
	public List<SaleEntity> month() {
		return salesService.getMonthlySales();
	}

	@Operation(summary = "Get daily sales summary", description = "Retrieves today's sales grouped by product, price, and shop")
	@GetMapping("/summary/day")
	public List<SalesSummaryDto> daySummary() {
		return salesService.getDailySalesSummary();
	}

	@Operation(summary = "Get weekly sales summary", description = "Retrieves last 7 days sales grouped by product, price, and shop")
	@GetMapping("/summary/week")
	public List<SalesSummaryDto> weekSummary() {
		return salesService.getWeeklySalesSummary();
	}

	@Operation(summary = "Get monthly sales summary", description = "Retrieves last month sales grouped by product, price, and shop")
	@GetMapping("/summary/month")
	public List<SalesSummaryDto> monthSummary() {
		return salesService.getMonthlySalesSummary();
	}
}