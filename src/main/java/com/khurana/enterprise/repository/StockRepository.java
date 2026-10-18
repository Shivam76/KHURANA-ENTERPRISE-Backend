package com.khurana.enterprise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khurana.enterprise.entity.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
	List<StockEntity> findByName(String name);

	StockEntity findByNameAndPrice(String name, double price);
}