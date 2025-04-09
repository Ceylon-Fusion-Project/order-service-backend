package com.finalproject.order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDto {
    private Long cartItemId;
    private Long productId;
    private Integer cartItemQuantity;
    private Double cartItemPrice;

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCartItemQuantity() {
        return cartItemQuantity;
    }

    public void setCartItemQuantity(int cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    public Double getCartItemPrice() {
        return cartItemPrice;
    }

    public void setCartItemPrice(double cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }

    public void setCartItemQuantity(Integer cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    public void setCartItemPrice(Double cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }
}
