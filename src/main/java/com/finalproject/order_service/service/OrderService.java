package com.finalproject.order_service.service;

import com.finalproject.order_service.dto.request.CartIdRequestDto;
import com.finalproject.order_service.dto.request.OrderRequestDto;
import com.finalproject.order_service.dto.response.OrderResponseDto;

public interface OrderService {
    OrderResponseDto placeOrderFromCart(CartIdRequestDto cartIdRequestDto);

    OrderResponseDto placeDirectOrder(OrderRequestDto orderRequestDto);


}
