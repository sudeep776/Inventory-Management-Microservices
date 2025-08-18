package com.microservices.orders.service;


import com.microservices.orders.dto.OrderRequestDto;
import com.microservices.orders.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    //create  order
    Boolean createOrder(OrderRequestDto orderRequestDto);

    //get orders
    List<OrderResponseDto> getOrders();
}
