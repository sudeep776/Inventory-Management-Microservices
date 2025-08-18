package com.microservices.orders.clients;

import com.microservices.orders.dto.InventoryRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory")
public interface InventoryClient {

    @GetMapping("/api/inventory/test")
    String testInventoryService(); // This method will call the test endpoint of the Inventory Service

    @PostMapping("/api/inventory/check")
    boolean checkInventory(@RequestBody InventoryRequestDto inventoryRequestDto); // This method will check if the product is in stock based on the SKU code

    @PostMapping("/api/inventory/reduce")
    boolean reduceStock(@RequestBody InventoryRequestDto inventoryRequestDto); // This method will reduce the stock for a given SKU code and quantity
}

