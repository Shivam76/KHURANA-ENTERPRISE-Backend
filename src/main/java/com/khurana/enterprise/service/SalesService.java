package com.khurana.enterprise.service;

import java.util.List;

import com.khurana.enterprise.dto.SalesSummaryDto;
import com.khurana.enterprise.entity.SaleEntity;

public interface SalesService {

	List<SaleEntity> getDailySales();

	List<SaleEntity> getWeeklySales();

	List<SaleEntity> getMonthlySales();

	List<SalesSummaryDto> getDailySalesSummary();

	List<SalesSummaryDto> getWeeklySalesSummary();

	List<SalesSummaryDto> getMonthlySalesSummary();
}
