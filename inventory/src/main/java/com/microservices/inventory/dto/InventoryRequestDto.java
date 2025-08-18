package com.microservices.inventory.dto;

import lombok.Data;

@Data
public class InventoryRequestDto {
    private String skuCode;
    private int quantity;
}
