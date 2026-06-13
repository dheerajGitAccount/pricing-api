package com.retail.pricing.repository;

import com.retail.pricing.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findBySaleDate(LocalDate saleDate);

    // Product with highest total revenue across all sales
    @Query("SELECT s.product.id FROM Sale s GROUP BY s.product.id ORDER BY SUM(s.totalRevenue) DESC LIMIT 1")
    Long findOptimumPriceProductId();

    @Query("SELECT SUM(s.totalRevenue) FROM Sale s WHERE s.product.id = :productId")
    Double totalRevenueByProduct(@Param("productId") Long productId);
}
