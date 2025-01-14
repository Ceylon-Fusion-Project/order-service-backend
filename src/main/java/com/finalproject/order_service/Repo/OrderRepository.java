package com.finalproject.order_service.Repo;

import com.finalproject.order_service.model.Order;
import com.finalproject.order_service.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
