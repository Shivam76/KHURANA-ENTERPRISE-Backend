package com.khurana.enterprise.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khurana.enterprise.entity.BillEntity;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {

	List<BillEntity> findByBillDateBetween(LocalDateTime start, LocalDateTime end);

	List<BillEntity> findByShopNameIgnoreCase(String shopName);
}