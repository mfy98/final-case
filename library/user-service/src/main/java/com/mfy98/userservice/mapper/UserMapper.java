package com.mfy98.userservice.mapper;

import com.mfy98.userservice.dto.CreateUserRequest;
import com.mfy98.userservice.dto.UpdateUserRequest;
import com.mfy98.userservice.dto.UserResponse;
import com.mfy98.userservice.entity.User;

public class UserMapper {
    public static User toEntity(CreateUserRequest req) {
        return User.builder()
                .username(req.getUsername())
                .name(req.getName())
                .email(req.getEmail())
                .role(req.getRole())
                .build();
    }

    public static void updateEntity(User user, UpdateUserRequest req) {
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setRole(req.getRole());
    }

    public static UserResponse toDto(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .name(u.getName())
                .email(u.getEmail())
                .role(u.getRole())
                .build();
    }
}