package com.klochkov.t1aop.service;

import com.klochkov.t1aop.entity.User;
import com.klochkov.t1aop.enumuration.StatusOrder;
import com.klochkov.t1aop.exception.ResourceNotFoundException;
import com.klochkov.t1aop.repository.UserRepository;
import com.klochkov.t1aop.service.impl.UserServiceImpl;
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
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers() {
        Pageable pageable = mock(Pageable.class);
        Page<User> expectedPage = new PageImpl<>(List.of(new User()));
        when(userRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<User> result = userService.getAllUsers(pageable);

        assertEquals(expectedPage, result);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void getUserById_found() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getUserById(id);

        assertEquals(user, result);
        verify(userRepository).findById(id);
    }

    @Test
    void getUserById_notFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(id));
        verify(userRepository).findById(id);
    }

    @Test
    void createUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        assertEquals(user, result);
        verify(userRepository).save(user);
    }

    @Test
    void deleteUserById_found() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.deleteUserById(id);

        verify(userRepository).unlinkOrdersFromUser(id);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUserById_notFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(id));
    }

    @Test
    void addOrder() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.addOrder(userId, orderId);

        verify(userRepository).addOrder(userId, orderId, StatusOrder.AVAILABLE);
        assertEquals(user, result);
    }
}
