package com.finalproject.order_service.feingClient;

import com.finalproject.order_service.dto.response.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE", path = "/api/v1/product")
public interface ProductClient {
    @GetMapping(path = "/get-product-details-by-id", params = "productId")
        ProductResponseDto getProductById(@RequestParam(value = "productId") Integer productId);
}
