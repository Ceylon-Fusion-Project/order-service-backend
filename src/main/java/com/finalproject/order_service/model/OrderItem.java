package com.finalproject.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "order_item_quantity")
    private Integer orderItemQuantity;
    @Column(name = "order_item_price")
    private Double orderItemPrice; // Capture price at order time

    @ManyToOne
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
