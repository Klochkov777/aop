package com.klochkov.t1aop.service.impl;

import com.klochkov.t1aop.entity.Order;
import com.klochkov.t1aop.exception.ResourceNotFoundException;
import com.klochkov.t1aop.repository.OrderRepository;
import com.klochkov.t1aop.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order getOrderById(UUID id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.orElseThrow(() ->
                new ResourceNotFoundException("Order with id `%s` not found".formatted(id)));
    }

    public List<Order> getOrdersByIds(List<UUID> ids) {
        return orderRepository.findAllById(ids);
    }

    public Order create(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id `%s` not found".formatted(id)));
        orderRepository.delete(order);
    }

    @Transactional
    @Override
    public void assignUser(UUID orderId, UUID userId) {
        int i = orderRepository.assignUser(orderId, userId);
    }

    @Transactional
    public void deleteOrdersByIds(List<UUID> ids) {
        orderRepository.deleteAllById(ids);
    }
}
