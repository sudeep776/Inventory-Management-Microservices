package com.microservices.product.dto;

import lombok.*;

@Data
public class Inventory {
    private Long productId;
    private String skuCode;
    private int quantity;
}
