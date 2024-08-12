package com.klochkov.t1aop.repository;

import com.klochkov.t1aop.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @EntityGraph(attributePaths = {"user"})
    @Override
    Optional<Order> findById(UUID id);

    @Modifying
    @Query("UPDATE Order o SET o.user.id = :userId WHERE o.id = :orderId")
    int assignUser(@Param("orderId") UUID orderId, @Param("userId") UUID userId);
}