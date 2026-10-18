package com.khurana.enterprise.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khurana.enterprise.entity.SaleEntity;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
	List<SaleEntity> findBySaleDateBetween(LocalDate start, LocalDate end);
}