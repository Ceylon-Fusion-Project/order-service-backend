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
    private CartItemRequestDto cartItem;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CartItemRequestDto getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItemRequestDto cartItem) {
        this.cartItem = cartItem;
    }
}
