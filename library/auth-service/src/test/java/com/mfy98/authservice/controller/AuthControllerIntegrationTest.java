package com.mfy98.authservice.controller;

import com.library.auth.AuthServiceApplication;
import com.library.auth.dto.AuthResponse;
import com.library.auth.dto.RegisterRequest;
import com.library.auth.dto.LoginRequest;
import com.library.auth.entity.User;
import com.library.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(
        classes = AuthServiceApplication.class,               // ← ana uygulama sınıfı
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "jwt.secret=B7sT5lsNO-YktTYcqUQn1NnkNerKFdlzT6qWwAcsHb0="
})
class AuthControllerIntegrationTest {

    @Autowired private TestRestTemplate rest;
    @Autowired private UserRepository repo;

    @BeforeEach
    void clean() {
        repo.deleteAll();
    }

    @Test
    void registerAndLogin() {
        RegisterRequest reg = new RegisterRequest("u","password","PATRON");
        ResponseEntity<Void> regResp = rest.postForEntity(
                "/auth/register", reg, Void.class);
        assertThat(regResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        LoginRequest login = new LoginRequest("u","password");
        ResponseEntity<AuthResponse> loginResp =
                rest.postForEntity("/auth/login", login, AuthResponse.class);

        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResp.getBody()).isNotNull();
        assertThat(loginResp.getBody().getToken()).isNotBlank();
    }

}
