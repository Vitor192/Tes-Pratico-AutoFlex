package com.example.inventory.repository;

import com.example.inventory.entity.ProductRawMaterial;
import com.example.inventory.entity.Product;
import com.example.inventory.entity.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRawMaterialRepository extends JpaRepository<ProductRawMaterial, Long> {
    List<ProductRawMaterial> findByProduct(Product product);
    List<ProductRawMaterial> findByRawMaterial(RawMaterial rawMaterial);
}
