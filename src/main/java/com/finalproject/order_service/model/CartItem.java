package com.finalproject.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id", nullable = false)
    private Long cartItemId;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(name = "cart_item_quantity", nullable = false)
    private Integer cartItemQuantity;
    @Column(name = "cart_item_price", nullable = false)
    private Double cartItemPrice;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
}
