package com.khurana.enterprise.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.khurana.enterprise.entity.BillEntity;
import com.khurana.enterprise.exception.InvalidRequestException;
import com.khurana.enterprise.repository.BillRepository;
import com.khurana.enterprise.service.BillService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

	private final BillRepository billRepo;

	@Override
	public List<BillEntity> getTodayBills() {
		log.info("Fetching today's bills");
		LocalDateTime start = LocalDate.now().atStartOfDay();
		LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
		log.info("Start: {} | End: {}", start, end);
		List<BillEntity> bill= billRepo.findByBillDateBetween(start, end);
		log.info("Found {} bills for today", bill.size());
		return bill;
	}

	@Override
	public List<BillEntity> getYesterdayBills() {
		log.info("Fetching yesterday's bills");
		LocalDate yesterday = LocalDate.now().minusDays(1);
		LocalDateTime start = yesterday.atStartOfDay();
		LocalDateTime end = yesterday.atTime(LocalTime.MAX);
		return billRepo.findByBillDateBetween(start, end);
	}

	@Override
	public List<BillEntity> getLastWeekBills() {
		log.info("Fetching last week's bills");
		LocalDateTime start = LocalDate.now().minusDays(7).atStartOfDay();
		LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
		return billRepo.findByBillDateBetween(start, end);
	}

	@Override
	public List<BillEntity> getLastMonthBills() {
		log.info("Fetching last month's bills");
		LocalDateTime start = LocalDate.now().minusMonths(1).atStartOfDay();
		LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
		return billRepo.findByBillDateBetween(start, end);
	}

	@Override
	public List<BillEntity> getBillsByMonth(int month, int year) {
		log.info("Fetching bills for month: {} year: {}", month, year);
		if (month < 1 || month > 12) {
			throw new InvalidRequestException("Month must be between 1 and 12");
		}
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
		return billRepo.findByBillDateBetween(start, end);
	}

	@Override
	public List<BillEntity> getBillsByYear(int year) {
		log.info("Fetching bills for year: {}", year);
		if (year < 2000 || year > 2100) {
			throw new InvalidRequestException("Year must be between 2000 and 2100");
		}
		LocalDateTime start = LocalDate.of(year, 1, 1).atStartOfDay();
		LocalDateTime end = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);
		return billRepo.findByBillDateBetween(start, end);
	}

	@Override
	public List<BillEntity> getBillsByShopName(String shopName) {
		log.info("Fetching bills for shop: {}", shopName);
		return billRepo.findByShopNameIgnoreCase(shopName);
	}
}
