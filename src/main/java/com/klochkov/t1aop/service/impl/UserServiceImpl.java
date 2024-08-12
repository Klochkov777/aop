package com.klochkov.t1aop.service.impl;

import com.klochkov.t1aop.entity.User;
import com.klochkov.t1aop.enumuration.StatusOrder;
import com.klochkov.t1aop.exception.ResourceNotFoundException;
import com.klochkov.t1aop.repository.UserRepository;
import com.klochkov.t1aop.service.UserService;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserById(UUID id) {
        Optional<User> orderOptional = userRepository.findById(id);
        return orderOptional.orElseThrow(() ->
                new ResourceNotFoundException("User with id `%s` not found".formatted(id)));
    }

    public List<User> getUsersByIds(List<UUID> ids) {
        return userRepository.findAllById(ids);
    }
    public User create(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(UUID id) {
        userRepository.unlinkOrdersFromUser(id);
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User with id `%s` not found".formatted(id)));
        userRepository.delete(user);
    }

    @Transactional
    public void deleteUsersByIds(List<UUID> ids) {
        userRepository.unlinkOrdersFromUsers(ids);
        userRepository.deleteAllById(ids);
    }

    @Transactional
    @Override
    public User addOrder(UUID userId, UUID orderId) {
        userRepository.addOrder(userId, orderId, StatusOrder.AVAILABLE);
        return getUserById(userId);
    }
}
