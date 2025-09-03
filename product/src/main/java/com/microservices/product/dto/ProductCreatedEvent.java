package com.microservices.product.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreatedEvent {
    private Long productId;
    private String skuCode;
    private Integer quantity;
}

