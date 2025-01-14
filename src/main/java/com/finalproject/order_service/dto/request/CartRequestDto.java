package com.finalproject.order_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDto {
    private Long userId;
    private List<CartItemRequestDto> cartItems;




    public Long getUserId() {
        return userId;
    }

    public List<CartItemRequestDto> getCartItems() {
        return cartItems;
    }
}
