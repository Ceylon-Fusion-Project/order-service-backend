package com.finalproject.order_service.service;

import com.finalproject.order_service.dto.request.OrderRequestDto;
import com.finalproject.order_service.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto placeOrderFromCart(Long userId);
    OrderResponseDto placeDirectOrder(OrderRequestDto orderRequestDto);
    List<OrderResponseDto> getOrdersByUserId(Long userId);
    OrderResponseDto cancelOrderByUserId(Long orderId);
    OrderResponseDto confirmOrderByUserId(Long orderId);
}
