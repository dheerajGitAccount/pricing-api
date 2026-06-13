package com.retail.pricing.controller;

import com.retail.pricing.dto.PriceConfigurationRequest;
import com.retail.pricing.model.PriceConfiguration;
import com.retail.pricing.service.PriceConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/price-configurations")
@RequiredArgsConstructor
public class PriceConfigurationController {

    private final PriceConfigurationService priceConfigurationService;

    @PutMapping
    public ResponseEntity<PriceConfiguration> addOrUpdate(@RequestBody PriceConfigurationRequest request) {
        return ResponseEntity.ok(priceConfigurationService.addOrUpdate(request));
    }
}
