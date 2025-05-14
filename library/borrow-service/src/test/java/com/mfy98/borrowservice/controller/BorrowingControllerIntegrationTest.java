package com.mfy98.borrowservice.controller;

import com.mfy98.borrowservice.dto.CreateBorrowingRequest;
import com.mfy98.borrowservice.dto.BorrowingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BorrowingControllerIntegrationTest {

    @Autowired
    private TestRestTemplate rest;
    @MockBean
    private RestTemplate bookRestTemplate;
    private Long savedId;

    @BeforeEach
    void setup() {
        when(bookRestTemplate.patchForObject(
                contains("/decrement"), isNull(), eq(Void.class)))
                .thenReturn(null);

        when(bookRestTemplate.patchForObject(
                contains("/increment"), isNull(), eq(Void.class)))
                .thenReturn(null);

        CreateBorrowingRequest req = new CreateBorrowingRequest(1L, 2L);
        ResponseEntity<BorrowingResponse> resp = rest.postForEntity(
                "/borrow", req, BorrowingResponse.class);
        savedId = resp.getBody().getId();
    }

    @Test
    void history_ShouldReturnList() {
        ResponseEntity<BorrowingResponse[]> resp = rest.getForEntity(
                "/borrow/history/2", BorrowingResponse[].class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotEmpty();
    }

    @Test
    void returnBook_ShouldMarkReturned() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> ent = new HttpEntity<>(headers);
        ResponseEntity<BorrowingResponse> resp = rest.exchange(
                "/borrow/return/" + savedId, HttpMethod.PUT, ent, BorrowingResponse.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody().getReturnDate()).isNotNull();
    }
}
