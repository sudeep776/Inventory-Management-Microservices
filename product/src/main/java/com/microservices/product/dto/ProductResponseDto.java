package com.microservices.product.dto;

import lombok.Data;

@Data
public class ProductResponseDto {
        private String id;
        private String name;
        private String description;
        private double price;
        private String skuCode; // Unique identifier to link with inventory
}
