package com.klochkov.t1aop.controller;

import com.klochkov.t1aop.dto.user.UserDto;
import com.klochkov.t1aop.entity.User;
import com.klochkov.t1aop.mapper.UserMapper;
import com.klochkov.t1aop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getList(Pageable pageable) {
        Page<User> allUsers = userService.getAllUsers(pageable);
        Page<UserDto> userDtos = userMapper.toPageDto(allUsers);
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOne(@PathVariable("id") UUID id) {
        User userById = userService.getUserById(id);
        UserDto dto = userMapper.toDto(userById);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<UserDto>> getMany(@RequestParam("ids") List<UUID> ids) {
        List<User> usersByIds = userService.getUsersByIds(ids);
        List<UserDto> listDto = userMapper.toListDto(usersByIds);
        return ResponseEntity.ok(listDto);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PostMapping("{id}/add_order")
    public ResponseEntity<UserDto> addOrder(@PathVariable("id")
                                            UUID userId,
                                            @RequestParam("order_id")
                                            UUID orderId) {
        User user = userService.addOrder(userId, orderId);
        UserDto dto = userMapper.toDto(user);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        userService.deleteUserById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteMany(@RequestParam List<UUID> ids) {
        userService.deleteUsersByIds(ids);
    }
}
