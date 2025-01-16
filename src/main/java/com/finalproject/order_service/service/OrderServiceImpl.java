package com.finalproject.order_service.service;

import com.finalproject.order_service.Repo.CartRepository;
import com.finalproject.order_service.Repo.OrderRepository;
import com.finalproject.order_service.dto.request.CartIdRequestDto;
import com.finalproject.order_service.dto.request.OrderRequestDto;
import com.finalproject.order_service.dto.response.OrderResponseDto;
import com.finalproject.order_service.dto.response.ProductResponseDto;
import com.finalproject.order_service.enums.OrderStatus;
import com.finalproject.order_service.feingClient.ProductClient;
import com.finalproject.order_service.model.Cart;
import com.finalproject.order_service.model.Order;
import com.finalproject.order_service.model.OrderItem;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductClient productClient;

    @Override
    @Transactional
    public OrderResponseDto placeOrderFromCart(CartIdRequestDto cartIdRequestDto) {
        // Retrieve the cart for the user
        Long cartId = cartIdRequestDto.getCartId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for cart ID: " + cartId));
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot place an order with an empty cart.");
        }

        cart.getCartItems().forEach(cartItem -> {
            ProductResponseDto product = productClient.getProductById(cartItem.getProductId());
            if (product.getAvailableQuantity() < cartItem.getCartItemQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product ID: " + cartItem.getProductId() +
                                ". Available: " + product.getAvailableQuantity() +
                                ", Requested: " + cartItem.getCartItemQuantity()
                );
            }
        });

        // Create a new order and save it to generate the orderId
        Order order = new Order();
        order.setUserId(cart.getUserId());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order); // Save the order to generate the orderId

        // Map cart items to order items and assign orderId
        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder); // Set the saved order
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setOrderItemQuantity(cartItem.getCartItemQuantity());
            orderItem.setOrderItemPrice(cartItem.getCartItemPrice()); // Fetch price dynamically
            return orderItem;
        }).collect(Collectors.toList());

        // Assign the order items to the order and save again
        savedOrder.setOrderItems(orderItems);
        Order finalSavedOrder = orderRepository.save(savedOrder);

        cartRepository.delete(cart);

        // Map the saved order to response DTO using ModelMapper
        return modelMapper.map(finalSavedOrder, OrderResponseDto.class);
    }

    @Override
    @Transactional
    public OrderResponseDto placeDirectOrder(OrderRequestDto orderRequestDto) {
        // Validate order items
        if (orderRequestDto.getOrderItems() == null || orderRequestDto.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }

        orderRequestDto.getOrderItems().forEach(itemDto -> {
            // Fetch product details from the Product Microservice using FeignClient
            ProductResponseDto product = productClient.getProductById(itemDto.getProductId());

            if (product == null) {
                throw new IllegalArgumentException("Product not found for ID: " + itemDto.getProductId());
            }

            if (product.getAvailableQuantity() < itemDto.getOrderItemQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product ID: " + itemDto.getProductId() +
                                ". Available: " + product.getAvailableQuantity() +
                                ", Requested: " + itemDto.getOrderItemQuantity()
                );
            }

            // Optionally, update item price with the product's price from the Product Microservice
            itemDto.setOrderItemPrice(product.getProductPrice());
        });
        // Create a new order and save it to generate the orderId
        Order order = new Order();
        order.setUserId(orderRequestDto.getUserId());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order); // Save the order to generate the orderId

        // Map order items from DTO and assign orderId
        List<OrderItem> orderItems = orderRequestDto.getOrderItems().stream().map(itemDto -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder); // Set the saved order
            orderItem.setProductId(itemDto.getProductId());
            orderItem.setOrderItemQuantity(itemDto.getOrderItemQuantity());
            orderItem.setOrderItemPrice(itemDto.getOrderItemPrice());
            return orderItem;
        }).collect(Collectors.toList());

        // Assign the order items to the order and save again
        savedOrder.setOrderItems(orderItems);
        Order finalSavedOrder = orderRepository.save(savedOrder);

        // Map the saved order to response DTO using ModelMapper
        return modelMapper.map(finalSavedOrder, OrderResponseDto.class);
    }

}

