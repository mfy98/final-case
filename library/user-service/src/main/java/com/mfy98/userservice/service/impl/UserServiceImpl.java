package com.mfy98.userservice.service.impl;

import com.mfy98.userservice.entity.User;
import com.mfy98.userservice.repository.UserRepository;
import com.mfy98.userservice.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository repo;
    public UserServiceImpl(UserRepository repo) { this.repo = repo; }

    @Override
    public User create(User u) { return repo.save(u); }

    @Override
    public Optional<User> findById(Long id) { return repo.findById(id); }

    @Override
    public List<User> listAll() { return repo.findAll(); }

    @Override
    public User update(Long id, User u) {
        u.setId(id);
        return repo.save(u);
    }

    @Override
    public void delete(Long id) { repo.deleteById(id); }

    @Override
    public boolean existsById(Long id) { return repo.existsById(id); }
}