package com.klochkov.t1aop.mapper;

import com.klochkov.t1aop.dto.user.UserDto;
import com.klochkov.t1aop.entity.User;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

    @AfterMapping
    default void linkOrders(@MappingTarget User user) {
        user.getOrders().forEach(order -> order.setUser(user));
    }

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);

    List<UserDto> toListDto(List<User> usersByIds);

    default Page<UserDto> toPageDto(Page<User> users) {
        return users.map(this::toDto);
    }
}