package com.finalproject.order_service.controller;

import com.finalproject.order_service.dto.request.OrderRequestDto;
import com.finalproject.order_service.dto.response.OrderResponseDto;
import com.finalproject.order_service.service.OrderService;
import com.finalproject.order_service.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping(
            path = "/get-orders/user",
            params = "userId"
          )
    public ResponseEntity<StandardResponse> getOrdersByUserId(@RequestParam (value = "userId") Long userId) {
        try {
            List<OrderResponseDto> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(new StandardResponse(HttpStatus.OK.value(), "Orders fetched successfully.", orders));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new StandardResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging framework
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch orders.", null));
        }
    }

    @PostMapping(
            path = "/cart-order/user",
            params = "userId"
    )
    public ResponseEntity<StandardResponse> placeOrderFromCart(@RequestParam (value = "userId") Long userId) {
        try {
            OrderResponseDto orderResponse = orderService.placeOrderFromCart(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new StandardResponse(HttpStatus.CREATED.value(), "Order placed from cart successfully.", orderResponse));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new StandardResponse(HttpStatus.BAD_REQUEST.value(), "Failed to place order from cart.", null));
        }
    }

    @PatchMapping("/direct-order/user")
    public ResponseEntity<StandardResponse> placeDirectOrder(@RequestBody OrderRequestDto orderRequestDto) {
        try {
            OrderResponseDto orderResponse = orderService.placeDirectOrder(orderRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new StandardResponse(HttpStatus.CREATED.value(), "Direct order placed successfully.", orderResponse));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new StandardResponse(HttpStatus.BAD_REQUEST.value(), "Failed to place direct order.", null));
        }
    }

    @PatchMapping(
            path = "/cancel-order/user",
            params = "orderId"
    )
    public ResponseEntity<StandardResponse> cancelOrderByUserId(@RequestParam (value = "orderId") Long orderId) {
        try {
            OrderResponseDto orderResponse = orderService.cancelOrderByUserId(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new StandardResponse(HttpStatus.OK.value(), "Order canceled successfully.", orderResponse));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new StandardResponse(HttpStatus.BAD_REQUEST.value(), "Failed to cancel order.", null));
        }
    }

    @PostMapping(
            path = "/confirm-order/user",
            params = "orderId"
    )
    public ResponseEntity<StandardResponse> confirmOrderByUserId(@RequestParam (value = "orderId") Long orderId) {
        try {
            OrderResponseDto orderResponse = orderService.confirmOrderByUserId(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new StandardResponse(HttpStatus.OK.value(), "Order confirmed successfully.", orderResponse));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new StandardResponse(HttpStatus.BAD_REQUEST.value(), "Failed to confirm order.", null));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new StandardResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null));
    }
}
