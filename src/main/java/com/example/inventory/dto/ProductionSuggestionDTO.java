package com.example.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionSuggestionDTO {
    private Long productId;
    private String productName;
    private Integer quantityToProduce;
    private BigDecimal totalValue;
}
