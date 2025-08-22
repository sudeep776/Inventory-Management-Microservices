package com.microservices.orders.service.serviceImpl;

import com.microservices.orders.clients.InventoryClient;
import com.microservices.orders.clients.ProductClient;
import com.microservices.orders.dto.InventoryRequestDto;
import com.microservices.orders.dto.OrderRequestDto;
import com.microservices.orders.dto.OrderResponseDto;
import com.microservices.orders.dto.Product;
import com.microservices.orders.entity.Order;
import com.microservices.orders.repository.OrderRepository;
import com.microservices.orders.service.OrderService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final InventoryClient inventoryClient;
    private final ProductClient productClient;

    @Retry(name = "inventoryRetry",fallbackMethod = "createOrderFallback") // Retry logic for inventory checks
    @RateLimiter(name = "inventoryRateLimiter", fallbackMethod = "createOrderFallback") // Rate limiting for inventory checks
    @Override
    public Boolean createOrder(OrderRequestDto orderRequestDto) {
        Long productId = orderRequestDto.getProductId();
        Product product = productClient.getProductById(productId);
        String skuCode = product.getSkuCode();
        int quantity = orderRequestDto.getQuantity();
        InventoryRequestDto inventoryRequestDto = new InventoryRequestDto(skuCode, quantity);
        // Check if the product is in stock
        boolean inStock = inventoryClient.checkInventory(inventoryRequestDto);
        if(inStock){
            // Reduce stock if in stock
            boolean stockReduced = inventoryClient.reduceStock(inventoryRequestDto);
            if(stockReduced){
                // Create order if stock is successfully reduced
                Order order = new Order();
                order.setQuantity(orderRequestDto.getQuantity());
                order.setSkuCode(product.getSkuCode());
                order.setPrice(BigDecimal.valueOf(product.getPrice() * quantity)); // set price from product
                order.setOrderNumber(UUID.randomUUID().toString()); // generate order number
                orderRepository.save(order);
                log.info("Order created successfully: {}", order);
                return true;
            } else {
                log.warn("Failed to reduce stock for product ID: {}", productId);
                return false;
            }
        } else {
            log.warn("Product with SKU code {} is not in stock", skuCode);
            return false;
        }
    }

    public Boolean createOrderFallback(OrderRequestDto orderRequestDto,Throwable throwable){
        log.info("Fallback occured due to : {}", throwable.getMessage());
        return false; // Fallback logic when inventory check fails
    }

    @Override
    public List<OrderResponseDto> getOrders() {
       List<Order> orders  = orderRepository.findAll();
       List<OrderResponseDto> orderResponseDtos= orders.stream().map(order ->modelMapper.map(order, OrderResponseDto.class)).toList();
         log.info("Retrieved orders: {}", orderResponseDtos);
         return orderResponseDtos;
    }


}
