package com.finalproject.order_service.service;

import com.finalproject.order_service.dto.request.CartRequestDto;
import com.finalproject.order_service.dto.request.OrderRequestDto;
import com.finalproject.order_service.dto.response.OrderResponseDto;

public interface OrderService {
    OrderResponseDto placeOrderFromCart(CartRequestDto cartRequestDto);

    OrderResponseDto placeDirectOrder(OrderRequestDto orderRequestDto);


}
