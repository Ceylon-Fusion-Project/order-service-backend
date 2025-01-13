package com.finalproject.order_service.service;

import com.finalproject.order_service.dto.request.CartRequestDto;
import com.finalproject.order_service.dto.response.CartResponseDto;

public interface CartService {
    CartResponseDto addToCart(CartRequestDto cartRequestDto);
}
