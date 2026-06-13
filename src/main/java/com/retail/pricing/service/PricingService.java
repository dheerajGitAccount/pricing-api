package com.retail.pricing.service;

import com.retail.pricing.dto.DailySalesSummary;
import com.retail.pricing.dto.OptimumPriceResponse;
import com.retail.pricing.model.Product;
import com.retail.pricing.model.Sale;
import com.retail.pricing.repository.ProductRepository;
import com.retail.pricing.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updatePrice(Long productId, double newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
//        product.setPrice(newPrice);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Sale recordSale(Long productId, int quantity, LocalDate saleDate) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        Sale sale = new Sale();
        sale.setProduct(product);
        sale.setQuantity(quantity);
        sale.setSaleDate(saleDate);
       // sale.setTotalRevenue(product.getPrice() * quantity);
        return saleRepository.save(sale);
    }

    public DailySalesSummary getDailySales(LocalDate date) {
        List<Sale> sales = saleRepository.findBySaleDate(date);
        int totalQty = sales.stream().mapToInt(Sale::getQuantity).sum();
        double totalRev = sales.stream().mapToDouble(Sale::getTotalRevenue).sum();
        return new DailySalesSummary(date, totalQty, totalRev);
    }

    public OptimumPriceResponse getOptimumPrice() {
        Long productId = saleRepository.findOptimumPriceProductId();
        if (productId == null) throw new RuntimeException("No sales data available");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        double totalRevenue = saleRepository.totalRevenueByProduct(productId);
        // product.getPrice()
        return new OptimumPriceResponse(product.getId(), product.getName(), 0.0, totalRevenue);
    }
}
