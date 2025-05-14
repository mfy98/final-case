package com.mfy98.authservice.controller;

import com.library.auth.controller.AuthController;
import com.library.auth.dto.LoginRequest;
import com.library.auth.dto.AuthResponse;
import com.library.auth.entity.User;
import com.library.auth.repository.UserRepository;
import com.library.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock private UserRepository userRepo;
    @Mock private JwtUtil jwtUtil;
    @InjectMocks private AuthController ctrl;
    private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

    @BeforeEach void init() { MockitoAnnotations.openMocks(this); }

    @Test
    void login_Success() {
        String raw = "pass";
        String hash = enc.encode(raw);
        User u = new User(1L, "u", hash, "ROLE");
        when(userRepo.findByUsername("u")).thenReturn(Optional.of(u));
        when(jwtUtil.generateToken("u","ROLE")).thenReturn("TOKEN");

        ResponseEntity<AuthResponse> resp = ctrl.login(new LoginRequest("u", raw));
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody().getToken()).isEqualTo("TOKEN");
    }

    @Test
    void login_Failure() {
        when(userRepo.findByUsername("x")).thenReturn(Optional.empty());
        var resp = ctrl.login(new LoginRequest("x","p"));
        assertThat(resp.getStatusCodeValue()).isEqualTo(401);
    }
}
