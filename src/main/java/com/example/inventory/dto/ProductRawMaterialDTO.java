package com.example.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRawMaterialDTO {
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private Long rawMaterialId;

    @NotNull
    @Positive
    private Integer quantityRequired;
}
