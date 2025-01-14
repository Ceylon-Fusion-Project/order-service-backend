package com.finalproject.order_service.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CartItemRequestDto {

    private Long productId;
    private int cartItemQuantity;
    private double cartItemPrice;

    public Integer getCartItemQuantity() {
        return cartItemQuantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public void setCartItemQuantity(int cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    public void setCartItemPrice(double cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public double getCartItemPrice() {
        return cartItemPrice;
    }
}
