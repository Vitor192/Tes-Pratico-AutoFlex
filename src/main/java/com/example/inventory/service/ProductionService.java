package com.example.inventory.service;

import com.example.inventory.dto.ProductionSuggestionDTO;
import com.example.inventory.entity.Product;
import com.example.inventory.entity.ProductRawMaterial;
import com.example.inventory.entity.RawMaterial;
import com.example.inventory.repository.ProductRawMaterialRepository;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductionService {

    private final ProductRepository productRepository;
    private final ProductRawMaterialRepository productRawMaterialRepository;
    private final RawMaterialRepository rawMaterialRepository;

    @Transactional(readOnly = true)
    public List<ProductionSuggestionDTO> suggestions() {
        List<Product> products = productRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());

        Map<Long, Integer> stock = rawMaterialRepository.findAll()
                .stream()
                .collect(Collectors.toMap(RawMaterial::getId, RawMaterial::getStockQuantity));

        List<ProductionSuggestionDTO> result = new ArrayList<>();

        for (Product product : products) {
            List<ProductRawMaterial> requirements = productRawMaterialRepository.findByProduct(product);
            if (requirements.isEmpty()) {
                continue;
            }
            int maxProduce = Integer.MAX_VALUE;
            for (ProductRawMaterial req : requirements) {
                Integer available = stock.getOrDefault(req.getRawMaterial().getId(), 0);
                int canMake = available / req.getQuantityRequired();
                if (canMake < maxProduce) {
                    maxProduce = canMake;
                }
            }
            if (maxProduce <= 0) {
                continue;
            }
            for (ProductRawMaterial req : requirements) {
                Long rmId = req.getRawMaterial().getId();
                int consumed = req.getQuantityRequired() * maxProduce;
                stock.put(rmId, stock.getOrDefault(rmId, 0) - consumed);
            }
            BigDecimal totalValue = product.getPrice().multiply(BigDecimal.valueOf(maxProduce));
            result.add(ProductionSuggestionDTO.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantityToProduce(maxProduce)
                    .totalValue(totalValue)
                    .build());
        }

        return result;
    }
}
