package com.retail.pricing.service;

import com.retail.pricing.dto.PriceConfigurationRequest;
import com.retail.pricing.model.PriceConfiguration;
import com.retail.pricing.model.Product;
import com.retail.pricing.repository.PriceConfigurationRepository;
import com.retail.pricing.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceConfigurationService {

    private final PriceConfigurationRepository priceConfigRepository;
    private final ProductRepository productRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public PriceConfiguration addOrUpdate(PriceConfigurationRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProductId()));

        List<PriceConfiguration> overlapping = priceConfigRepository.findOverlapping(
                request.getProductId(), request.getStartDate(), request.getEndDate());

        List<PriceConfiguration> toAdd    = new ArrayList<>();
        List<PriceConfiguration> toDelete = new ArrayList<>();

        for (PriceConfiguration existing : overlapping) {
            boolean fullyOverlapped =
                    !existing.getStartDate().isBefore(request.getStartDate()) &&
                    !existing.getEndDate().isAfter(request.getEndDate());

            if (fullyOverlapped) {
                toDelete.add(existing);

            } else if (existing.getStartDate().isBefore(request.getStartDate()) &&
                       existing.getEndDate().isAfter(request.getEndDate())) {
                // Split existing into left and right parts
                PriceConfiguration left = new PriceConfiguration();
                left.setProduct(existing.getProduct());
                left.setPrice(existing.getPrice());
                left.setCurrency(existing.getCurrency());
                left.setStartDate(existing.getStartDate());
                left.setEndDate(request.getStartDate().minusDays(1));
                toAdd.add(left);

                PriceConfiguration right = new PriceConfiguration();
                right.setProduct(existing.getProduct());
                right.setPrice(existing.getPrice());
                right.setCurrency(existing.getCurrency());
                right.setStartDate(request.getEndDate().plusDays(1));
                right.setEndDate(existing.getEndDate());
                toAdd.add(right);

                toDelete.add(existing);

            } else if (!existing.getStartDate().isBefore(request.getStartDate())) {
                // Trim start of existing
                PriceConfiguration trimmed = new PriceConfiguration();
                trimmed.setProduct(existing.getProduct());
                trimmed.setPrice(existing.getPrice());
                trimmed.setCurrency(existing.getCurrency());
                trimmed.setStartDate(request.getEndDate().plusDays(1));
                trimmed.setEndDate(existing.getEndDate());
                toAdd.add(trimmed);

                toDelete.add(existing);

            } else {
                // Trim end of existing
                PriceConfiguration trimmed = new PriceConfiguration();
                trimmed.setProduct(existing.getProduct());
                trimmed.setPrice(existing.getPrice());
                trimmed.setCurrency(existing.getCurrency());
                trimmed.setStartDate(existing.getStartDate());
                trimmed.setEndDate(request.getStartDate().minusDays(1));
                toAdd.add(trimmed);

                toDelete.add(existing);
            }
        }

        // New configuration to insert
        PriceConfiguration newConfig = new PriceConfiguration();
        newConfig.setProduct(product);
        newConfig.setPrice(request.getPrice());
        newConfig.setCurrency(request.getCurrency());
        newConfig.setStartDate(request.getStartDate());
        newConfig.setEndDate(request.getEndDate());
        toAdd.add(newConfig);

        // Execute deletes then adds in sequence
        priceConfigRepository.deleteAll(toDelete);
        List<PriceConfiguration> saved = priceConfigRepository.saveAll(toAdd);

        // Return the newly inserted config (last in the toAdd list)
        return saved.get(saved.size() - 1);
    }
}
