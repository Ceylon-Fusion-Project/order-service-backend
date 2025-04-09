package com.finalproject.order_service.service;

import com.finalproject.order_service.dto.request.OrderRequestDto;
import com.finalproject.order_service.dto.response.OrderResponseDto;
import com.finalproject.order_service.enums.OrderStatus;
import com.finalproject.order_service.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderResponseDto placeOrderFromCart(Long userId);
    OrderResponseDto placeDirectOrder(OrderRequestDto orderRequestDto);
    List<OrderResponseDto> getOrdersByUserId(Long userId);
    OrderResponseDto cancelOrderByUserId(Long orderId);
    OrderResponseDto confirmOrderByUserId(Long orderId);
    Page<OrderResponseDto> getAllOrders(Optional<OrderStatus> orderStatus, Pageable pageable);
}
