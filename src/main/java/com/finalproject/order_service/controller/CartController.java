package com.finalproject.order_service.controller;

import com.finalproject.order_service.dto.request.CartRequestDto;
import com.finalproject.order_service.dto.request.RemoveCartItemRequestDto;
import com.finalproject.order_service.dto.response.CartResponseDto;
import com.finalproject.order_service.service.CartService;
import com.finalproject.order_service.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add-item-to-cart")
    public ResponseEntity<StandardResponse> addToCart(@RequestBody CartRequestDto cartRequestDto) {
        // Validate input
        if (cartRequestDto == null || cartRequestDto.getUserId() == null || cartRequestDto.getCartItem() == null) {
            return ResponseEntity.badRequest().body(
                    new StandardResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request: User ID and cart item must be provided.", null)
            );
        }

        try {
            // Call service to add to cart
            CartResponseDto cartResponseDto = cartService.addToCart(cartRequestDto);
            return ResponseEntity.ok(
                    new StandardResponse(HttpStatus.OK.value(), "Item added to cart successfully.", cartResponseDto)
            );
        } catch (IllegalArgumentException e) {
            // Handle validation exceptions
            return ResponseEntity.badRequest().body(
                    new StandardResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null)
            );
        } catch (Exception e) {
            // Log and return a generic error
            e.printStackTrace(); // Replace with proper logging framework (e.g., SLF4J)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new StandardResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to add item to cart.", null)
            );
        }
    }

    @DeleteMapping("/remove-item-from-cart")
    public ResponseEntity<StandardResponse> removeCartItem(@RequestBody RemoveCartItemRequestDto removeCartItemRequestDto) {
        // Validate input
        if (removeCartItemRequestDto == null || removeCartItemRequestDto.getUserId() == null || removeCartItemRequestDto.getProductId() == null) {
            return ResponseEntity.badRequest().body(
                    new StandardResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request: User ID and product ID must be provided.", null)
            );
        }

        try {
            // Call the service method to remove the cart item
            CartResponseDto updatedCart = cartService.removeCartItem(removeCartItemRequestDto);
            // Return the updated cart along with a success status
            return ResponseEntity.ok(
                    new StandardResponse(HttpStatus.OK.value(), "Item removed from cart successfully.", updatedCart)
            );
        } catch (IllegalArgumentException e) {
            // Handle validation exceptions
            return ResponseEntity.badRequest().body(
                    new StandardResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null)
            );
        } catch (Exception e) {
            // Log and return a generic error
            e.printStackTrace(); // Replace with proper logging framework (e.g., SLF4J)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new StandardResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to remove item from cart.", null)
            );
        }
    }

    @GetMapping(
            path = "/get-cart-items/user",
            params = "userId"
    )
    public ResponseEntity<StandardResponse> getCartItemsByUserId(@RequestParam(value = "userId") Long userId) {
        // Validate the input
        if (userId == null) {
            return ResponseEntity.badRequest().body(
                    new StandardResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request: User ID must be provided.", null)
            );
        }
        try {
            // Call the service method to fetch cart items
            CartResponseDto cartResponseDto = cartService.getCartItemsByUserId(userId);
            return ResponseEntity.ok(
                    new StandardResponse(HttpStatus.OK.value(), "Cart items fetched successfully.", cartResponseDto)
            );
        } catch (IllegalArgumentException e) {
            // Handle validation exceptions
            return ResponseEntity.badRequest().body(
                    new StandardResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null)
            );
        } catch (Exception e) {
            // Log and return a generic error
            e.printStackTrace(); // Replace with proper logging framework (e.g., SLF4J)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new StandardResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch cart items.", null)
            );
        }
    }

}
