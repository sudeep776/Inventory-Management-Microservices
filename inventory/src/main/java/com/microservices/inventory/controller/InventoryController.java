package com.microservices.inventory.controller;

import com.microservices.inventory.dto.InventoryRequestDto;
import com.microservices.inventory.entity.Inventory;
import com.microservices.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    // Add methods to handle requests, e.g., checking stock availability
    @PostMapping("/check")
    public boolean checkInventory(@RequestBody InventoryRequestDto inventoryRequestDto) {
        // This is a placeholder method. You can implement actual logic to check inventory.
        return inventoryService.isInStock(inventoryRequestDto);
    }

    @PostMapping("/reduce")
    public boolean reduceStock(@RequestBody InventoryRequestDto inventoryRequestDto) {
        return inventoryService.reduceStock(inventoryRequestDto);
    }

    @GetMapping("/test")
    public String test(){
        return "Inventory Service is running";
    }

    @PostMapping("/create")
    public boolean createInventory(@RequestBody Inventory inventory) {
        return inventoryService.createInventory(inventory);
    }
}
