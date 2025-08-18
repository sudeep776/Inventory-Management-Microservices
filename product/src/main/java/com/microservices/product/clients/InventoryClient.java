package com.microservices.product.clients;

import com.microservices.product.dto.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory")
public interface InventoryClient {

    @GetMapping("/api/inventory/test")
    String testInventoryService(); // This method will call the test endpoint of the Inventory Service

    @PostMapping("/api/inventory/create")
    boolean createInventory(@RequestBody Inventory inventory); // This method will create a new inventory entry
}

