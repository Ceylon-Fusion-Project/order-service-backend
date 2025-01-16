package com.finalproject.order_service.controller;

import com.finalproject.order_service.dto.request.CartRequestDto;
import com.finalproject.order_service.dto.request.RemoveCartItemRequestDto;
import com.finalproject.order_service.dto.response.CartResponseDto;
import com.finalproject.order_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartRequestDto cartRequestDto) {
        // Validate input
        if (cartRequestDto == null || cartRequestDto.getUserId() == null || cartRequestDto.getCartItems() == null) {
            return ResponseEntity.badRequest().body("Invalid request: User ID and cart items must be provided.");
        }

        try {
            // Call service to add to cart
            CartResponseDto cartResponseDto = cartService.addToCart(cartRequestDto);
            return ResponseEntity.ok(cartResponseDto);
        } catch (IllegalArgumentException e) {
            // Handle validation exceptions
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Log and return a generic error
            e.printStackTrace(); // Replace with logging framework (e.g., SLF4J)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add item to cart.");
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartResponseDto> removeCartItem(@RequestBody RemoveCartItemRequestDto removeCartItemRequestDto) {

            // Call the service method to remove the cart item
            CartResponseDto updatedCart = cartService.removeCartItem(removeCartItemRequestDto);

            // Return the updated cart along with a success status
            return ResponseEntity.ok(updatedCart);

    }

}

