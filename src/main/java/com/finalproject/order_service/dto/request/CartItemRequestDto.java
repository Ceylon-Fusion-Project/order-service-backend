package com.finalproject.order_service.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequestDto {

    private Long productId;
    private Integer cartItemQuantity;
    private Double cartItemPrice;

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

    public Double getCartItemPrice() {
        return cartItemPrice;
    }

    public void setCartItemQuantity(Integer cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    public void setCartItemPrice(Double cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }
}
