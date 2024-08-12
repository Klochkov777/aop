package com.klochkov.t1aop.controller;

import com.klochkov.t1aop.dto.order.OrderDto;
import com.klochkov.t1aop.entity.Order;
import com.klochkov.t1aop.mapper.OrderMapper;
import com.klochkov.t1aop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    public Page<OrderDto> getList(Pageable pageable) {
        Page<Order> allOrders = orderService.getAllOrders(pageable);
        Page<OrderDto> orderDtos = orderMapper.toPageOrderDto(allOrders);
        return orderDtos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOne(@PathVariable("id") UUID id) {
        Order orderById = orderService.getOrderById(id);
        OrderDto dto = orderMapper.toDto(orderById);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<OrderDto>> getMany(@RequestParam List<UUID> ids) {
        List<Order> ordersByIds = orderService.getOrdersByIds(ids);
        List<OrderDto> listDto = orderMapper.toListDto(ordersByIds);
        return ResponseEntity.ok(listDto);
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.create(order);
    }

    @PostMapping("{id}/assign_user")
    @ResponseStatus(HttpStatus.OK)
    public void assignUser(@PathVariable("id") UUID id,
                                               @RequestParam("user_id") UUID userId) {
        orderService.assignUser(id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        orderService.deleteOrderById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteMany(@RequestParam List<UUID> ids) {
        orderService.deleteOrdersByIds(ids);
    }
}
