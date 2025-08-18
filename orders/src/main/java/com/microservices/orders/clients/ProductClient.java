package com.microservices.orders.clients;

import com.microservices.orders.dto.InventoryRequestDto;
import com.microservices.orders.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product")
public interface ProductClient {
    @GetMapping("/api/products/{id}")
    public Product getProductById(@PathVariable Long id);
}

