package com.finalproject.order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WishlistResponseDto {
    private Integer productId;
    private String productName;
    private Double productPrice;
    private LocalDateTime createdAt;
}
