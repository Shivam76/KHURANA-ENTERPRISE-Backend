package com.khurana.enterprise.service;

import com.khurana.enterprise.dto.BillCreationDto;
import com.khurana.enterprise.entity.BillEntity;

public interface BillingService {

    BillEntity createBill(
        String shopName,
        BillCreationDto request,
        boolean gstApplied,
        String customerGstNumber,
        boolean skipStockCheck
    );
}
