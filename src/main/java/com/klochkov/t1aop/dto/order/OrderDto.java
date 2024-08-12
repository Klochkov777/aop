package com.klochkov.t1aop.dto.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.klochkov.t1aop.entity.Order;
import com.klochkov.t1aop.entity.User;
import com.klochkov.t1aop.enumuration.StatusOrder;
import lombok.Value;

import java.util.UUID;

/**
 * DTO for {@link Order}
 */
@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDto {
    UUID id;
    String description;
    StatusOrder status;
    UserDto user;

    /**
     * DTO for {@link User}
     */
    @Value
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UserDto {
        UUID id;
        String name;
        String email;
    }
}