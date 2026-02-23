package com.example.inventory.controller;

import com.example.inventory.dto.ProductRawMaterialDTO;
import com.example.inventory.service.ProductRawMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/product-raw-materials")
@RequiredArgsConstructor
public class ProductRawMaterialController {

    private final ProductRawMaterialService productRawMaterialService;

    @PostMapping
    public ResponseEntity<ProductRawMaterialDTO> create(@Valid @RequestBody ProductRawMaterialDTO dto) {
        ProductRawMaterialDTO saved = productRawMaterialService.create(dto);
        return ResponseEntity.created(URI.create("/product-raw-materials/" + saved.getId())).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productRawMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
