package com.finalproject.order_service.Repo;

import com.finalproject.order_service.enums.OrderStatus;
import com.finalproject.order_service.model.Order;
import com.finalproject.order_service.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
    Optional<Object> findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
}
