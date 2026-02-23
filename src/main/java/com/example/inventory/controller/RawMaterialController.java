package com.example.inventory.controller;

import com.example.inventory.dto.RawMaterialDTO;
import com.example.inventory.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/raw-materials")
@RequiredArgsConstructor
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @GetMapping
    public ResponseEntity<List<RawMaterialDTO>> list() {
        return ResponseEntity.ok(rawMaterialService.listAll());
    }

    @PostMapping
    public ResponseEntity<RawMaterialDTO> create(@Valid @RequestBody RawMaterialDTO dto) {
        RawMaterialDTO saved = rawMaterialService.create(dto);
        return ResponseEntity.created(URI.create("/raw-materials/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialDTO> update(@PathVariable Long id, @Valid @RequestBody RawMaterialDTO dto) {
        return ResponseEntity.ok(rawMaterialService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rawMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
