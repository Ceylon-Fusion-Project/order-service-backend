package com.finalproject.order_service.service;

import com.finalproject.order_service.Repo.CartItemRepository;
import com.finalproject.order_service.Repo.CartRepository;
import com.finalproject.order_service.dto.request.CartItemRequestDto;
import com.finalproject.order_service.dto.request.CartRequestDto;
import com.finalproject.order_service.dto.response.CartResponseDto;
import com.finalproject.order_service.model.Cart;
import com.finalproject.order_service.model.CartItem;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public CartResponseDto addToCart(CartRequestDto cartRequestDto) {
        // Validate the request DTO
        validateCartRequest(cartRequestDto);

        // Retrieve or create a new cart for the user
        Cart cart = (Cart) cartRepository.findByUserId(cartRequestDto.getUserId())
                .orElseGet(() -> createNewCart(cartRequestDto.getUserId()));

        // Process and update cart items
        cartRequestDto.getCartItems().forEach(cartItemRequestDto -> processCartItem(cart, cartItemRequestDto));

        // Save the updated cart
        Cart savedCart = cartRepository.save(cart);

        // Return the updated cart mapped to a response DTO
        return modelMapper.map(savedCart, CartResponseDto.class);
    }

    private void validateCartRequest(CartRequestDto cartRequestDto) {
        if (cartRequestDto.getUserId() == null || cartRequestDto.getCartItems() == null || cartRequestDto.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("User ID and cart items must not be null or empty.");
        }

        boolean hasInvalidItems = cartRequestDto.getCartItems().stream()
                .anyMatch(item -> item.getProductId() == null || item.getCartItemQuantity() <= 0);

        if (hasInvalidItems) {
            throw new IllegalArgumentException("Each cart item must have a valid product ID and a quantity greater than zero.");
        }
    }

    private Cart createNewCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartRepository.save(cart);

    }

    private void processCartItem(Cart cart, CartItemRequestDto cartItemRequestDto) {
        Long productId = cartItemRequestDto.getProductId();
        int quantity = cartItemRequestDto.getCartItemQuantity();
        double price = cartItemRequestDto.getCartItemPrice();

        // Find if the cart already contains the item
        Optional<CartItem> existingItemOptional = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItemOptional.isPresent()) {
            // Update the quantity if the item exists
            CartItem existingItem = existingItemOptional.get();
            existingItem.setCartItemQuantity(existingItem.getCartItemQuantity() + quantity);
        } else {
            // Create and add a new item to the cart
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setCartItemQuantity(quantity);
            newItem.setCartItemPrice(price);
            newItem.setCart(cart);

            cart.addCartItem(newItem); // Use the convenience method to maintain bidirectional consistency
        }
    }
}
