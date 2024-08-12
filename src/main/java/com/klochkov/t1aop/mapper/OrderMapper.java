package com.klochkov.t1aop.mapper;

import com.klochkov.t1aop.entity.Order;
import com.klochkov.t1aop.dto.order.OrderDto;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Order toEntity(OrderDto orderDto);

    OrderDto toDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order partialUpdate(OrderDto orderDto, @MappingTarget Order order);

    List<OrderDto> toListDto(List<Order> orders);

    default Page<OrderDto> toPageOrderDto(Page<Order> orders) {
        return orders.map(this::toDto);
    }
}