package com.khurana.enterprise.service;

import java.util.List;

import com.khurana.enterprise.entity.BillEntity;

public interface BillService {

	List<BillEntity> getTodayBills();

	List<BillEntity> getYesterdayBills();

	List<BillEntity> getLastWeekBills();

	List<BillEntity> getLastMonthBills();

	List<BillEntity> getBillsByMonth(int month, int year);

	List<BillEntity> getBillsByYear(int year);

	List<BillEntity> getBillsByShopName(String shopName);
}
