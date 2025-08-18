package com.microservices.inventory.repository;

import com.microservices.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    boolean existsBySkuCodeAndQuantityGreaterThan(String skuCode, int quantity);

    Optional<Inventory> findBySkuCode(String skuCode);
}
