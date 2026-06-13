package com.retail.pricing.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PriceConfigurationRequest {
    private Long productId;
    private double price;
    private String currency;
    private LocalDate startDate;
    private LocalDate endDate;
}
