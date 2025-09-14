package com.microservices.orders.controller;

import com.microservices.orders.clients.InventoryClient;
import com.microservices.orders.dto.OrderRequestDto;
import com.microservices.orders.dto.OrderResponseDto;
import com.microservices.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final InventoryClient inventoryClient;

    @PostMapping
    public String createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        // This method will handle order creation logic
        orderService.createOrder(orderRequestDto);
        return "Order created successfully";
    }

    @GetMapping
    public List<OrderResponseDto> getOrders() {
        // This method will handle fetching all orders
        return orderService.getOrders();
    }

    @GetMapping("/testAuth")
    @PreAuthorize("hasRole('USER')")
    public String testInventoryService() {
        // This method will call the Inventory Service's test endpoint
        return inventoryClient.testInventoryService();
    }


}
