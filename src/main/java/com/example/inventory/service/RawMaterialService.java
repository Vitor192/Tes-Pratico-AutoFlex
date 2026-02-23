package com.example.inventory.service;

import com.example.inventory.dto.RawMaterialDTO;
import com.example.inventory.entity.RawMaterial;
import com.example.inventory.exception.NotFoundException;
import com.example.inventory.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RawMaterialService {
    private final RawMaterialRepository rawMaterialRepository;

    @Transactional(readOnly = true)
    public List<RawMaterialDTO> listAll() {
        return rawMaterialRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RawMaterialDTO create(RawMaterialDTO dto) {
        RawMaterial rm = new RawMaterial();
        rm.setName(dto.getName());
        rm.setStockQuantity(dto.getStockQuantity());
        RawMaterial saved = rawMaterialRepository.save(rm);
        return toDTO(saved);
    }

    @Transactional
    public RawMaterialDTO update(Long id, RawMaterialDTO dto) {
        RawMaterial rm = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RawMaterial not found: " + id));
        rm.setName(dto.getName());
        rm.setStockQuantity(dto.getStockQuantity());
        return toDTO(rm);
    }

    @Transactional
    public void delete(Long id) {
        if (!rawMaterialRepository.existsById(id)) {
            throw new NotFoundException("RawMaterial not found: " + id);
        }
        rawMaterialRepository.deleteById(id);
    }

    private RawMaterialDTO toDTO(RawMaterial rm) {
        return RawMaterialDTO.builder()
                .id(rm.getId())
                .name(rm.getName())
                .stockQuantity(rm.getStockQuantity())
                .build();
    }
}
