package com.klochkov.t1aop.service;

import com.klochkov.t1aop.entity.Order;
import com.klochkov.t1aop.exception.ResourceNotFoundException;
import com.klochkov.t1aop.repository.OrderRepository;
import com.klochkov.t1aop.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void getAllOrders() {
        Pageable pageable = mock(Pageable.class);
        Page<Order> expectedPage = new PageImpl<>(List.of(new Order()));
        when(orderRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Order> result = orderService.getAllOrders(pageable);

        assertEquals(expectedPage, result);
        verify(orderRepository).findAll(pageable);
    }

    @Test
    void getOrderById_found() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(id);

        assertEquals(order, result);
        verify(orderRepository).findById(id);
    }

    @Test
    void getOrderById_notFound() {
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(id));
        verify(orderRepository).findById(id);
    }

    @Test
    void createOrder() {
        Order order = new Order();
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.create(order);

        assertEquals(order, result);
        verify(orderRepository).save(order);
    }

    @Test
    void deleteOrderById_found() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        orderService.deleteOrderById(id);

        verify(orderRepository).delete(order);
    }

    @Test
    void deleteOrderById_notFound() {
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrderById(id));
    }

    @Test
    void assignUser() {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(orderRepository.assignUser(orderId, userId)).thenReturn(1);

        orderService.assignUser(orderId, userId);

        verify(orderRepository).assignUser(orderId, userId);
    }
}
