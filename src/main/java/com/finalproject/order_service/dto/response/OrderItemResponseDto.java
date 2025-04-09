package com.finalproject.order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {
    private Long orderItemId;
    private Long productId;
    private Integer orderItemQuantity;
    private Double orderItemPrice;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setOrderItemQuantity(int orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }

    public Double getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(double orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }

    public void setOrderItemQuantity(Integer orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }

    public void setOrderItemPrice(Double orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }
}
