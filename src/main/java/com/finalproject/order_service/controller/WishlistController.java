package com.finalproject.order_service.controller;

import com.finalproject.order_service.dto.request.WishlistRequestDto;
import com.finalproject.order_service.dto.response.WishlistResponseDto;
import com.finalproject.order_service.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add-item-to-wishlist")
    public ResponseEntity<String> addToWishlist(@RequestBody WishlistRequestDto wishlistRequestDto) {
        try {
            String response = wishlistService.addToWishlist(wishlistRequestDto);
            return ResponseEntity.ok(response);
        }
        catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while adding to wishlist.");
        }
    }

    @PostMapping("/remove-item-from-wishlist")
    public ResponseEntity<String> removeFromWishlist(@RequestBody WishlistRequestDto wishlistRequestDto) {
        try {
            String response = wishlistService.removeFromWishlist(wishlistRequestDto);
            return ResponseEntity.ok(response);
        }
        catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while removing from wishlist.");
        }
    }

    @GetMapping("get-wishlist/{userId}")
    public ResponseEntity<List<WishlistResponseDto>> getWishlist(@PathVariable Long userId) {
        try {
            List<WishlistResponseDto> wishlist = wishlistService.getWishlist(userId);
            return ResponseEntity.ok(wishlist);
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while fetching the wishlist.");
        }
    }
}
