package com.retail.pricing.controller;

import com.retail.pricing.dto.DailySalesSummary;
import com.retail.pricing.dto.OptimumPriceResponse;
import com.retail.pricing.model.Product;
import com.retail.pricing.model.Sale;
import com.retail.pricing.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(pricingService.saveProduct(product));
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(pricingService.getAllProducts());
    }

    @PutMapping("/products/{id}/price")
    public ResponseEntity<Product> updatePrice(@PathVariable Long id, @RequestParam double price) {
        return ResponseEntity.ok(pricingService.updatePrice(id, price));
    }

    @PostMapping("/sales")
    public ResponseEntity<Sale> recordSale(
            @RequestParam Long productId,
            @RequestParam int quantity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate saleDate) {
        return ResponseEntity.ok(pricingService.recordSale(productId, quantity, saleDate));
    }

    @GetMapping("/sales/daily")
    public ResponseEntity<DailySalesSummary> getDailySales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(pricingService.getDailySales(date));
    }

    @GetMapping("/sales/optimum-price")
    public ResponseEntity<OptimumPriceResponse> getOptimumPrice() {
        return ResponseEntity.ok(pricingService.getOptimumPrice());
    }
}
