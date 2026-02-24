package com.example.inventory.service;

import com.example.inventory.entity.Product;
import com.example.inventory.entity.ProductRawMaterial;
import com.example.inventory.entity.RawMaterial;
import com.example.inventory.dto.ProductionSuggestionDTO;
import com.example.inventory.repository.ProductRawMaterialRepository;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.RawMaterialRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProductionServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RawMaterialRepository rawMaterialRepository;
    @Autowired
    private ProductRawMaterialRepository productRawMaterialRepository;
    @Autowired
    private ProductionService productionService;

    @Test
    void suggestions_respects_price_priority_and_stock_consumption() {
        RawMaterial steel = rawMaterialRepository.save(RawMaterial.builder().name("Steel").stockQuantity(10).build());
        RawMaterial plastic = rawMaterialRepository.save(RawMaterial.builder().name("Plastic").stockQuantity(5).build());

        Product premium = productRepository.save(Product.builder().name("Premium").price(new BigDecimal("100.00")).build());
        Product basic = productRepository.save(Product.builder().name("Basic").price(new BigDecimal("50.00")).build());

        productRawMaterialRepository.save(ProductRawMaterial.builder().product(premium).rawMaterial(steel).quantityRequired(3).build());
        productRawMaterialRepository.save(ProductRawMaterial.builder().product(premium).rawMaterial(plastic).quantityRequired(2).build());

        productRawMaterialRepository.save(ProductRawMaterial.builder().product(basic).rawMaterial(steel).quantityRequired(2).build());

        List<ProductionSuggestionDTO> suggestions = productionService.suggestions();

        assertThat(suggestions).isNotEmpty();
        assertThat(suggestions.get(0).getProductName()).isEqualTo("Premium");
        // For Premium: min(10/3, 5/2) = min(3, 2) = 2
        assertThat(suggestions.get(0).getQuantityToProduce()).isEqualTo(2);

        // After consuming for Premium: steel: 10 - (3*2)=4, plastic: 5 - (2*2)=1
        // For Basic: min(4/2) = 2
        assertThat(suggestions).anySatisfy(s -> {
            if (s.getProductName().equals("Basic")) {
                assertThat(s.getQuantityToProduce()).isEqualTo(2);
            }
        });
    }
}
