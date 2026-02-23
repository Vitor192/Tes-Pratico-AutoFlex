package com.example.inventory.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventoryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Industrial Inventory API")
                        .description("REST API for products and raw materials management")
                        .version("v1")
                        .license(new License().name("Apache 2.0")));
    }

    @Bean
    public GroupedOpenApi inventoryGroup() {
        return GroupedOpenApi.builder()
                .group("inventory")
                .pathsToMatch("/**")
                .build();
    }
}
