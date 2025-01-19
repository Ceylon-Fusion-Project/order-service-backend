package com.finalproject.order_service.feingClient;

import com.finalproject.order_service.dto.response.ProductResponseDto;

public class ProductClientFallback implements ProductClient {
    @Override
    public ProductResponseDto getProductById(Integer productId) {
        return null;
    }
}
