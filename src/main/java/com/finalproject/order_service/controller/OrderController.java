package com.finalproject.order_service.controller;

import com.finalproject.order_service.dto.request.OrderRequestDto;
import com.finalproject.order_service.dto.response.OrderResponseDto;
import com.finalproject.order_service.enums.OrderStatus;
import com.finalproject.order_service.model.Order;
import com.finalproject.order_service.service.OrderService;
import com.finalproject.order_service.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping(
            path = "/get-all-orders/admin",
            params = {"orderStatus", "page", "size"}
    )
    public ResponseEntity<StandardResponse> getAllOrders(
            @RequestParam Optional<OrderStatus> orderStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<OrderResponseDto> orders = orderService.getAllOrders(orderStatus, pageable);

            // Success response with the paginated orders
            return ResponseEntity.status(HttpStatus.OK).body(
                    new StandardResponse(HttpStatus.OK.value(), "Orders fetched successfully.", orders));
        } catch (RuntimeException ex) {
            // Handle runtime exceptions and send a bad request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new StandardResponse(HttpStatus.BAD_REQUEST.value(), "Failed to fetch orders.", null));
        } catch (Exception ex) {
            // Handle unexpected errors with an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new StandardResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred.", null));
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

    @PostMapping("/direct-order/user")
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

    @PatchMapping(
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
