package com.microservices.inventory.service.serviceImpl;

import com.microservices.inventory.dto.ProductCreatedEvent;
import com.microservices.inventory.entity.Inventory;
import com.microservices.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryEventConsumer {

    private final InventoryRepository inventoryRepository;

    @KafkaListener(topics = "product-created", groupId = "inventory-group")
    public void consume(ProductCreatedEvent event) {
        log.info("📥 Event received: {}", event);

        Inventory inventory = new Inventory();
        inventory.setProductId(event.getProductId());
        inventory.setSkuCode(event.getSkuCode());
        inventory.setQuantity(event.getQuantity());

        inventoryRepository.save(inventory);
        log.info("✅ Inventory created for product {}", event.getProductId());
    }
}

