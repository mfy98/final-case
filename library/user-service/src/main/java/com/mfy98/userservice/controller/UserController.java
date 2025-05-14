package com.mfy98.userservice.controller;

import com.mfy98.userservice.dto.CreateUserRequest;
import com.mfy98.userservice.dto.UpdateUserRequest;
import com.mfy98.userservice.dto.UserResponse;
import com.mfy98.userservice.entity.User;
import com.mfy98.userservice.exception.ResourceNotFoundException;
import com.mfy98.userservice.mapper.UserMapper;
import com.mfy98.userservice.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest req) {
        log.info("Creating user: username={}, name={}, role={}",
                req.getUsername(), req.getName(), req.getRole());
        User user = UserMapper.toEntity(req);
        User created = userService.create(user);
        UserResponse resp = UserMapper.toDto(created);
        log.info("User created successfully: id={}, username={}", resp.getId(), resp.getUsername());

        URI location = URI.create("/users/" + resp.getId());
        return ResponseEntity.created(location).body(resp);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Fetching all users");
        List<UserResponse> users = userService.listAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
        log.debug("Found {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("Fetching user by id={}", id);
        User user = userService.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found: id={}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });
        UserResponse dto = UserMapper.toDto(user);
        log.info("User fetched: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest req) {
        log.info("Updating user id={}: new name={}, new email={}, new role={}",
                id, req.getName(), req.getEmail(), req.getRole());
        User existing = userService.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot update, user not found: id={}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        UserMapper.updateEntity(existing, req);
        User updated = userService.update(id, existing);
        UserResponse dto = UserMapper.toDto(updated);
        log.info("User updated successfully: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user id={}", id);
        if (!userService.existsById(id)) {
            log.warn("Cannot delete, user not found: id={}", id);
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userService.delete(id);
        log.info("User deleted: id={}", id);
        return ResponseEntity.noContent().build();
    }
}
