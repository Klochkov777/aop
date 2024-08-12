package com.klochkov.t1aop.service;

import com.klochkov.t1aop.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface OrderService {
    Page<Order> getAllOrders(Pageable pageable);

    Order getOrderById(UUID id) ;

    List<Order> getOrdersByIds(List<UUID> ids);

    Order create(Order order);

    void deleteOrderById(UUID id);

    void assignUser(UUID orderId, UUID userId);

    void deleteOrdersByIds(List<UUID> ids);
}
