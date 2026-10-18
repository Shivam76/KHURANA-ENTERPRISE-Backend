package com.khurana.enterprise.serviceimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.khurana.enterprise.dto.SalesSummaryDto;
import com.khurana.enterprise.entity.SaleEntity;
import com.khurana.enterprise.repository.SaleRepository;
import com.khurana.enterprise.service.SalesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {

	private final SaleRepository saleRepository;

	@Override
	public List<SaleEntity> getDailySales() {
		LocalDate today = LocalDate.now();
		log.info("Fetching daily sales for date: {}", today);
		return saleRepository.findBySaleDateBetween(today, today);
	}

	@Override
	public List<SaleEntity> getWeeklySales() {
		LocalDate today = LocalDate.now();
		LocalDate weekAgo = today.minusDays(7);
		log.info("Fetching weekly sales from {} to {}", weekAgo, today);
		return saleRepository.findBySaleDateBetween(weekAgo, today);
	}

	@Override
	public List<SaleEntity> getMonthlySales() {
		LocalDate today = LocalDate.now();
		LocalDate monthAgo = today.minusMonths(1);
		log.info("Fetching monthly sales from {} to {}", monthAgo, today);
		return saleRepository.findBySaleDateBetween(monthAgo, today);
	}

	@Override
	public List<SalesSummaryDto> getDailySalesSummary() {
		log.info("Fetching daily sales summary");
		return groupSales(getDailySales());
	}

	@Override
	public List<SalesSummaryDto> getWeeklySalesSummary() {
		log.info("Fetching weekly sales summary");
		return groupSales(getWeeklySales());
	}

	@Override
	public List<SalesSummaryDto> getMonthlySalesSummary() {
		log.info("Fetching monthly sales summary");
		return groupSales(getMonthlySales());
	}

	private List<SalesSummaryDto> groupSales(List<SaleEntity> sales) {
		return sales.stream()
				.collect(Collectors.groupingBy(
						sale -> sale.getProductName() + "|" + sale.getPrice() + "|" + sale.getShopName() + "|" + sale.getSaleDate()))
				.values().stream()
				.map(group -> {
					SaleEntity first = group.get(0);
					int totalQty = group.stream().mapToInt(SaleEntity::getQuantity).sum();
					double totalAmt = group.stream().mapToDouble(SaleEntity::getTotalAmount).sum();
					return new SalesSummaryDto(
							first.getProductName(),
							first.getPrice(),
							totalQty,
							totalAmt,
							first.getShopName(),
							first.getSaleDate());
				})
				.toList();
	}
}
