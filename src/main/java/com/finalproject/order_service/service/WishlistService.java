package com.finalproject.order_service.service;

import com.finalproject.order_service.dto.request.WishlistRequestDto;
import com.finalproject.order_service.dto.response.WishlistResponseDto;

import java.util.List;

public interface WishlistService {
    String addToWishlist(WishlistRequestDto wishlistRequestDTO);
    String removeFromWishlist(WishlistRequestDto wishlistRequestDTO);
    List<WishlistResponseDto> getWishlist(Long userId);
}
