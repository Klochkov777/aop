package com.klochkov.t1aop.service;

import com.klochkov.t1aop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.UUID;


public interface UserService {
    Page<User> getAllUsers(Pageable pageable);

    User getUserById(UUID id) ;

    List<User> getUsersByIds(List<UUID> ids);

    User create(User user);

    void deleteUserById(UUID id);

    void deleteUsersByIds(List<UUID> ids);

    User addOrder(UUID userId, UUID orderId);
}
