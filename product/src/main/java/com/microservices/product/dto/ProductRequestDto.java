package com.microservices.product.dto;

import lombok.Data;

@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private double price;
    private String skuCode; // Unique identifier for the product in inventory
    private int quantity; // Quantity of the product available in inventory
}
