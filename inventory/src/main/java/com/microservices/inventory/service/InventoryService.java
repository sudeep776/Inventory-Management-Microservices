package com.microservices.inventory.service;

import com.microservices.inventory.dto.InventoryRequestDto;
import com.microservices.inventory.entity.Inventory;

public interface InventoryService {
    boolean isInStock(InventoryRequestDto inventoryRequestDto);
    boolean reduceStock(InventoryRequestDto inventoryRequestDto);
    boolean createInventory(Inventory inventory);
}
