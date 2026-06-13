package com.retail.pricing.repository;

import com.retail.pricing.model.PriceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceConfigurationRepository extends JpaRepository<PriceConfiguration, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p FROM PriceConfiguration p
            WHERE p.product.id = :productId
            AND p.startDate <= :endDate
            AND p.endDate >= :startDate
            """)
    List<PriceConfiguration> findOverlapping(
            @Param("productId") Long productId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    Optional<PriceConfiguration> findByProductIdAndStartDateAndEndDate(
            Long productId, LocalDate startDate, LocalDate endDate);
}
