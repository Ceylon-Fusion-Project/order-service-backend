package com.finalproject.order_service.service.serviceIMPL;

import com.finalproject.order_service.Repo.CartItemRepository;
import com.finalproject.order_service.Repo.CartRepository;
import com.finalproject.order_service.dto.request.CartItemRequestDto;
import com.finalproject.order_service.dto.request.CartRequestDto;
import com.finalproject.order_service.dto.request.RemoveCartItemRequestDto;
import com.finalproject.order_service.dto.response.CartResponseDto;
import com.finalproject.order_service.model.Cart;
import com.finalproject.order_service.model.CartItem;
import com.finalproject.order_service.service.CartService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        validateCartRequest(cartRequestDto);

        // Fetch or create a cart
        Cart cart = (Cart) cartRepository.findByUserId(cartRequestDto.getUserId())
                .orElseGet(() -> createNewCart(cartRequestDto.getUserId()));

        // Initialize the cart items list if it's null
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }

        // Process the cart item addition
        CartItemRequestDto cartItemRequestDto = cartRequestDto.getCartItem();
        processCartItem(cart, cartItemRequestDto);

        // Save the updated cart
        Cart savedCart = cartRepository.save(cart);
        return modelMapper.map(savedCart, CartResponseDto.class);
    }

    @Override
    @Transactional
    public CartResponseDto removeCartItem(RemoveCartItemRequestDto removeCartItemRequestDto) {
        Long userId = removeCartItemRequestDto.getUserId();
        Long productId = removeCartItemRequestDto.getProductId();

        // Fetch cart and validate
        Cart cart = (Cart) cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user ID: " + userId));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("The cart is empty. Cannot remove product ID: " + productId);
        }

        // Find the item to remove
        Optional<CartItem> itemToRemoveOptional = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        // Remove the item if it exists
        if (itemToRemoveOptional.isPresent()) {
            CartItem itemToRemove = itemToRemoveOptional.get();
            cart.getCartItems().remove(itemToRemove);
            cartItemRepository.delete(itemToRemove);

            // Save the updated cart
            Cart updatedCart = cartRepository.save(cart);
            return modelMapper.map(updatedCart, CartResponseDto.class);
        } else {
            throw new IllegalArgumentException("Product ID: " + productId + " not found in the cart.");
        }
    }

    @Override
    @Transactional
    public CartResponseDto getCartItemsByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }

        // Fetch the cart
        Cart cart = (Cart) cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user ID: " + userId));

        // Ensure the cart has items, initialize if necessary
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            cart.setCartItems(new ArrayList<>());
        }

        // Return cart as DTO
        return modelMapper.map(cart, CartResponseDto.class);
    }

    private void validateCartRequest(CartRequestDto cartRequestDto) {
        if (cartRequestDto.getUserId() == null || cartRequestDto.getCartItem() == null) {
            throw new IllegalArgumentException("User ID and cart item must not be null.");
        }

        CartItemRequestDto cartItem = cartRequestDto.getCartItem();
        if (cartItem.getProductId() == null || cartItem.getCartItemQuantity() <= 0) {
            throw new IllegalArgumentException("Cart item must have a valid product ID and a quantity greater than zero.");
        }
    }

    private Cart createNewCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCartItems(new ArrayList<>());
        return cartRepository.save(cart);
    }

    private void processCartItem(Cart cart, CartItemRequestDto cartItemRequestDto) {
        Long productId = cartItemRequestDto.getProductId();
        Integer quantity = cartItemRequestDto.getCartItemQuantity();
        Double price = cartItemRequestDto.getCartItemPrice();

        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }

        // Check if the item already exists in the cart
        Optional<CartItem> existingItemOptional = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItemOptional.isPresent()) {
            CartItem existingItem = existingItemOptional.get();
            existingItem.setCartItemQuantity(existingItem.getCartItemQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setCartItemPrice(price);
            newItem.setCartItemQuantity(quantity);
            newItem.setCart(cart);

            // Add the new item to the cart
            cart.getCartItems().add(newItem);
        }
    }
}
