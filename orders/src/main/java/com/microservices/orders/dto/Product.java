package com.microservices.orders.dto;

import lombok.*;
import org.springframework.data.annotation.Id;


@Data
public class Product {
    @Id
    private Long id;
    private String name;
    private String description;
    private double price;
    private String skuCode; // Stock Keeping Unit Code, used for inventory management
}
