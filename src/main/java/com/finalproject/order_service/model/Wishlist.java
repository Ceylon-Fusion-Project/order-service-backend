package com.finalproject.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "wishlist")
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id",nullable = false)
    private Long wishlistId;
    @Column(name = "user_id",nullable = false)
    private Long userId;
    @Column(name = "product_id",nullable = false)
    private Integer productId;
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;
}
