package com.mfy98.bookservice.controller;

import com.mfy98.bookservice.BookServiceApplication;
import com.mfy98.bookservice.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = BookServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestPropertySource(properties = {
        // Reactive Security otomatik konfigürasyonlarını devre dışı bırak
        "spring.autoconfigure.exclude=" +
                "org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration," +
                "org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration," +
                "org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration",

        // Test ortamında security’yi tamamen kapat
        "spring.security.enabled=false"
})
class BookControllerIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void createAndGetById_ShouldReturnCreatedBook() {
        Book newBook = Book.builder()
                .title("Integration")
                .author("Tester")
                .isbn("INT-001")
                .genre("Test")
                .totalCopies(5)
                .build();

        // POST → 201 Created
        Book created = webClient.post().uri("/books")
                .bodyValue(newBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();

        // GET → 200 OK
        webClient.get().uri("/books/{id}", created.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Integration");
    }

    @Test
    void rentAndReturn_ShouldUpdateStock() {
        Book b = webClient.post().uri("/books")
                .bodyValue(Book.builder()
                        .title("Stock")
                        .author("Tester")
                        .isbn("STK-01")
                        .genre("Test")
                        .totalCopies(2)
                        .build())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .returnResult()
                .getResponseBody();

        Long id = b.getId();

        // Kirala (decrement)
        webClient.patch().uri("/books/{id}/decrement", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.availableCopies").isEqualTo(1);

        // İade (increment)
        webClient.patch().uri("/books/{id}/increment", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.availableCopies").isEqualTo(2);
    }
}
