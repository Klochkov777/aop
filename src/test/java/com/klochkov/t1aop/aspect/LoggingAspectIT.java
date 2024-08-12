package com.klochkov.t1aop.aspect;

import com.klochkov.t1aop.entity.Order;
import com.klochkov.t1aop.entity.User;
import com.klochkov.t1aop.exception.ResourceNotFoundException;
import com.klochkov.t1aop.repository.OrderRepository;
import com.klochkov.t1aop.repository.UserRepository;
import com.klochkov.t1aop.service.OrderService;
import com.klochkov.t1aop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
@EnableAspectJAutoProxy
public class LoggingAspectIT {
    @Autowired
    private OrderService orderService;

    @MockBean
    private LoggingAspect loggingAspect;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void getOrderById_shouldCallAspectMethods() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        orderService.getOrderById(id);

        verify(loggingAspect, times(1)).findOrderByIdBefore(any(), any());

    }


    @Test
    void getOrderById_whenNotFound_shouldCallAspectMethodsWithException() {
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(id));

        verify(loggingAspect, times(1)).findOrderByIdBefore(any(), any());
    }

    @Test
    void getUserById_shouldCallAspectMethods() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.getUserById(id);

        verify(loggingAspect, times(1)).findUserByIdBefore(any(), any());
    }

    @Test
    void getUserById_whenNotFound_shouldCallAspectMethodsWithException() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(id));

        verify(loggingAspect, times(1)).findUserByIdBefore(any(), any());
    }

    @Test
    void addOrder_shouldCallAspectMethods() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.addOrder(userId, orderId);

        verify(loggingAspect, times(1)).addOrderOfUserServiceAtStart(any());
    }
}
