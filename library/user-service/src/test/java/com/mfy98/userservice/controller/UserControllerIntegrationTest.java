package com.mfy98.userservice.controller;

import com.mfy98.userservice.dto.CreateUserRequest;
import com.mfy98.userservice.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserControllerIntegrationTest {

    @Autowired private TestRestTemplate rest;

    @Test
    void createAndGetUser() {
        CreateUserRequest req = new CreateUserRequest(
                "furkan", "Mehmet Furkan Yigit", "mfurkanyigit98@gmail.com", "PATRON"
        );

        ResponseEntity<UserResponse> resp = rest.postForEntity(
                "/users", req, UserResponse.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        UserResponse body = resp.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getUsername()).isEqualTo("furkan");
    }
}
