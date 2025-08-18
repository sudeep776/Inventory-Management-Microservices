package com.microservices.inventory.service.serviceImpl;

import com.microservices.inventory.dto.InventoryRequestDto;
import com.microservices.inventory.entity.Inventory;
import com.microservices.inventory.repository.InventoryRepository;
import com.microservices.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public boolean isInStock(InventoryRequestDto inventoryRequestDto) {
        String skuCode = inventoryRequestDto.getSkuCode();
        int quantity = inventoryRequestDto.getQuantity();
        return inventoryRepository.existsBySkuCodeAndQuantityGreaterThan(skuCode, quantity);
    }

    @Override
    public boolean reduceStock(InventoryRequestDto inventoryRequestDto) {
        String skuCode = inventoryRequestDto.getSkuCode();
        int quantity = inventoryRequestDto.getQuantity();

        return inventoryRepository.findBySkuCode(skuCode).map(inventory -> {
            if (inventory.getQuantity() >= quantity) {
                inventory.setQuantity(inventory.getQuantity() - quantity);
                inventoryRepository.save(inventory);
                log.info("Stock reduced for {} by {} units", skuCode, quantity);
                return true;
            } else {
                log.warn("Insufficient stock for product: {}", skuCode);
                return false;
            }
        }).orElseGet(() -> {
            log.warn("Product not found: {}", skuCode);
            return false;
        });
    }

    @Override
    public boolean createInventory(Inventory inventory) {
        if (inventory == null || inventory.getSkuCode() == null || inventory.getQuantity() < 0) {
            log.error("Invalid inventory data provided");
            return false;
        }
        try {
            inventoryRepository.save(inventory);
            log.info("Inventory created successfully for SKU: {}", inventory.getSkuCode());
            return true;
        } catch (Exception e) {
            log.error("Error creating inventory for SKU: {} - {}", inventory.getSkuCode(), e.getMessage());
            return false;
        }
    }
}
