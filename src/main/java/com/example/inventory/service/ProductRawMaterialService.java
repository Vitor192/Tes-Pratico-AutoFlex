package com.example.inventory.service;

import com.example.inventory.dto.ProductRawMaterialDTO;
import com.example.inventory.entity.Product;
import com.example.inventory.entity.ProductRawMaterial;
import com.example.inventory.entity.RawMaterial;
import com.example.inventory.exception.NotFoundException;
import com.example.inventory.repository.ProductRawMaterialRepository;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductRawMaterialService {

    private final ProductRawMaterialRepository productRawMaterialRepository;
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    @Transactional
    public ProductRawMaterialDTO create(ProductRawMaterialDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found: " + dto.getProductId()));
        RawMaterial rm = rawMaterialRepository.findById(dto.getRawMaterialId())
                .orElseThrow(() -> new NotFoundException("RawMaterial not found: " + dto.getRawMaterialId()));

        if (dto.getQuantityRequired() == null || dto.getQuantityRequired() <= 0) {
            throw new IllegalArgumentException("quantityRequired must be positive");
        }

        ProductRawMaterial prm = new ProductRawMaterial();
        prm.setProduct(product);
        prm.setRawMaterial(rm);
        prm.setQuantityRequired(dto.getQuantityRequired());
        ProductRawMaterial saved = productRawMaterialRepository.save(prm);

        dto.setId(saved.getId());
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        if (!productRawMaterialRepository.existsById(id)) {
            throw new NotFoundException("ProductRawMaterial not found: " + id);
        }
        productRawMaterialRepository.deleteById(id);
    }
}
