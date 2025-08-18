package com.microservices.orders.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequestDto {
    private Long productId;
    private Integer quantity;
}
