package com.mfy98.userservice.service;

import com.mfy98.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User create(User u);
    Optional<User> findById(Long id);
    List<User> listAll();
    User update(Long id, User u);
    void delete(Long id);
    boolean existsById(Long id);
}