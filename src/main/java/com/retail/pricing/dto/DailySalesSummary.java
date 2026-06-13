package com.retail.pricing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailySalesSummary {
    private LocalDate date;
    private int totalQuantity;
    private double totalRevenue;
}
