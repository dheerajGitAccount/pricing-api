package com.retail.pricing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptimumPriceResponse {
    private Long productId;
    private String productName;
    private double price;
    private double totalRevenue;
}
