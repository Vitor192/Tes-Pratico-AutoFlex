package com.example.inventory.controller;

import com.example.inventory.dto.ProductionSuggestionDTO;
import com.example.inventory.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/production")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionService productionService;

    @GetMapping("/suggestions")
    public ResponseEntity<List<ProductionSuggestionDTO>> suggestions() {
        return ResponseEntity.ok(productionService.suggestions());
    }
}
