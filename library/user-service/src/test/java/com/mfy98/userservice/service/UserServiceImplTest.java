package com.mfy98.userservice.service;

import com.mfy98.userservice.entity.User;
import com.mfy98.userservice.repository.UserRepository;
import com.mfy98.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserServiceImplTest {

    @Mock
    private UserRepository repo;
    @InjectMocks
    private UserServiceImpl svc;

    @BeforeEach
    void init() { MockitoAnnotations.openMocks(this); }

    @Test
    void create_ShouldSaveUser() {
        User u = User.builder().username("u").name("n").email("e@e").role("PATRON").build();
        when(repo.save(u)).thenReturn(u);
        var created = svc.create(u);
        assertThat(created).isEqualTo(u);
    }

    @Test
    void findById_WhenExists() {
        User u = new User(1L,"u","n","e@e","LIB");
        when(repo.findById(1L)).thenReturn(Optional.of(u));
        assertThat(svc.findById(1L)).contains(u);
    }

    @Test
    void existsById_ShouldDelegate() {
        when(repo.existsById(5L)).thenReturn(true);
        assertThat(svc.existsById(5L)).isTrue();
    }

    @Test
    void delete_ShouldCallRepo() {
        svc.delete(2L);
        verify(repo).deleteById(2L);
    }
}
