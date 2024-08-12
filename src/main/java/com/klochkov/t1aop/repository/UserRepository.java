package com.klochkov.t1aop.repository;

import com.klochkov.t1aop.entity.User;
import com.klochkov.t1aop.enumuration.StatusOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {"orders"})
    Page<User> findAll(Pageable pageable);

    @Modifying
    @Query("UPDATE Order o SET o.user.id = :userId WHERE o.id = :orderId AND o.status = :status")
    void addOrder(@Param("userId") UUID userId,
                  @Param("orderId") UUID orderId,
                  @Param("status") StatusOrder statusOrder);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Order o SET o.user = NULL WHERE o.user.id = :userId")
    int unlinkOrdersFromUser(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    void deleteUserById(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE Order o SET o.user = NULL WHERE o.user.id IN :uuids")
    int unlinkOrdersFromUsers(@Param("uuids") List<UUID> userIds);
}