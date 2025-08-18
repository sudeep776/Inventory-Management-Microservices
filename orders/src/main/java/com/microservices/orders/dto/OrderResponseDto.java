package com.microservices.orders.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderResponseDto {
    private Long id;
    private String orderNumber;
    private String skuCode;
    private Integer quantity;
    private BigDecimal price;
}
