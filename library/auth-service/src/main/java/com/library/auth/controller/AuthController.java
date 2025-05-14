package com.library.auth.controller;

import com.library.auth.dto.AuthResponse;
import com.library.auth.dto.LoginRequest;
import com.library.auth.dto.RegisterRequest;
import com.library.auth.entity.User;
import com.library.auth.repository.UserRepository;
import com.library.auth.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        log.info("Login attempt for username={}", req.getUsername());
        return userRepo.findByUsername(req.getUsername())
                .filter(u -> {
                    boolean matches = encoder.matches(req.getPassword(), u.getPassword());
                    if (!matches) {
                        log.warn("Password mismatch for username={}", req.getUsername());
                    }
                    return matches;
                })
                .map(u -> {
                    String token = jwtUtil.generateToken(u.getUsername(), u.getRole());
                    log.info("Login successful for username={}", u.getUsername());
                    return ResponseEntity.ok(new AuthResponse(token));
                })
                .orElseGet(() -> {
                    log.warn("Login failed for username={}", req.getUsername());
                    return ResponseEntity.status(401).build();
                });
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
        log.info("Register attempt for username={}", req.getUsername());
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            log.warn("Registration conflict, username already exists: {}", req.getUsername());
            return ResponseEntity.status(409).build();
        }
        User u = User.builder()
                .username(req.getUsername())
                .password(encoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();
        userRepo.save(u);
        log.info("User registered successfully: username={}", u.getUsername());
        return ResponseEntity.status(201).build();
    }
}
