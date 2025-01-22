package com.finalproject.order_service.service.serviceIMPL;

import com.finalproject.order_service.Repo.CartRepository;
import com.finalproject.order_service.Repo.OrderRepository;
import com.finalproject.order_service.dto.request.OrderRequestDto;
import com.finalproject.order_service.dto.response.OrderItemResponseDto;
import com.finalproject.order_service.dto.response.OrderResponseDto;
import com.finalproject.order_service.enums.OrderStatus;
import com.finalproject.order_service.model.Cart;
import com.finalproject.order_service.model.Order;
import com.finalproject.order_service.model.OrderItem;
import com.finalproject.order_service.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class   OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderResponseDto placeOrderFromCart(Long userId) {
        // Retrieve the cart for the user
        Cart cart = (Cart) cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user ID: " + userId));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot place an order with an empty cart.");
        }

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
            orderItem.setOrderItemPrice(cartItem.getCartItemPrice());
            return orderItem;
        }).collect(Collectors.toList());

        // Assign the order items to the order and save again
        savedOrder.setOrderItems(orderItems);
        Order finalSavedOrder = orderRepository.save(savedOrder);

        // Delete the cart after the order is placed
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

    @Override
    @Transactional
    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        // Retrieve all orders for the given user ID
        List<Order> orders = orderRepository.findByUserId(userId);

        if (orders.isEmpty()) {
            throw new IllegalArgumentException("No orders found for user ID: " + userId);
        }

        // Map the orders to response DTOs
        List<OrderResponseDto> orderResponseDto = orders.stream()
                .map(order -> modelMapper.map(order, OrderResponseDto.class))
                .collect(Collectors.toList());

        return orderResponseDto;
    }

    @Override
    @Transactional
    public OrderResponseDto cancelOrderByUserId(Long orderId) {
        // Fetch the order for the given order ID
        Order order = (Order) orderRepository.findByOrderIdAndOrderStatus(orderId, OrderStatus.PENDING)
                .orElseThrow(() -> new IllegalArgumentException("No pending order found for order ID: " + orderId));

        // Update the order status to canceled
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setOrderDate(LocalDateTime.now()); // Update order date to the cancellation time

        // Remove canceled items from the order's items
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.removeIf(item -> item.getOrder().getOrderId().equals(orderId));

        order.setOrderItems(orderItems);

        // Save the updated order
        orderRepository.save(order);

        // Map the order to the response DTO
        return modelMapper.map(order, OrderResponseDto.class);
    }
    @Transactional
    @Override
    public OrderResponseDto confirmOrderByUserId(Long orderId) {

            // Fetch the order for the given order ID
            Order order = (Order) orderRepository.findByOrderIdAndOrderStatus(orderId, OrderStatus.PENDING)
                    .orElseThrow(() -> new IllegalArgumentException("No pending order found for order ID: " + orderId));

            // Update the order status to confirmed
            order.setOrderStatus(OrderStatus. CONFIRMED);
            order.setOrderDate(LocalDateTime.now()); // Update order date to the confirmation time

            // Save the updated order
            orderRepository.save(order);

            // Map the order to the response DTO
            return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    @Transactional
    public Page<OrderResponseDto> getAllOrders(Optional<OrderStatus> orderStatus, Pageable pageable) {
        Page<Order> orders;

        // Fetch orders based on orderStatus or all orders
        if (orderStatus.isPresent()) {
            orders = orderRepository.findByOrderStatus(orderStatus.get(), pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }

        // Map each Order entity to OrderResponseDto using ModelMapper
        List<OrderResponseDto> orderDtos = orders.getContent().stream()
                .map(order -> {
                    OrderResponseDto orderDto = modelMapper.map(order, OrderResponseDto.class);

                    // Map the OrderItems collection to OrderItemResponseDto using ModelMapper
                    List<OrderItemResponseDto> orderItemDtos = order.getOrderItems().stream()
                            .map(orderItem -> modelMapper.map(orderItem, OrderItemResponseDto.class))
                            .collect(Collectors.toList());

                    orderDto.setOrderItems(orderItemDtos);
                    return orderDto;
                })
                .collect(Collectors.toList());

        // Return the page of OrderResponseDto
        return new PageImpl<>(orderDtos);
    }
}