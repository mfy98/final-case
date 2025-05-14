package com.mfy98.bookservice.service;

import com.mfy98.bookservice.entity.Book;
import com.mfy98.bookservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository repo;

    @InjectMocks
    private BookService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void rentOne_WhenStockAvailable_ShouldDecrement() {
        // 1) findById ile öncelikle stoku 2 olan bir kitap dön
        Book existing = Book.builder()
                .id(1L)
                .totalCopies(5)
                .availableCopies(2)
                .build();
        when(repo.findById(1L)).thenReturn(Mono.just(existing));

        // 2) save çağırıldığında, argümandaki Book'u aynen geri döndür
        when(repo.save(any(Book.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        // 3) Service’i çağır ve availableCopies’in 1’e düştüğünü doğrula
        StepVerifier.create(service.borrowOne(1L))
                .expectNextMatches(b -> b.getAvailableCopies() == 1)
                .verifyComplete();

        // Ayrıca save’in gerçekten yeni availableCopies ile çağrıldığını kontrol edebiliriz
        verify(repo).save(argThat(b -> b.getAvailableCopies() == 1 && b.getId().equals(1L)));
    }

    @Test
    void rentOne_WhenNoStock_ShouldError() {
        // 1) findById ile availableCopies = 0 dön
        Book existing = Book.builder()
                .id(1L)
                .totalCopies(5)
                .availableCopies(0)
                .build();
        when(repo.findById(1L)).thenReturn(Mono.just(existing));

        // 2) Servisin hata atmasını bekle
        StepVerifier.create(service.borrowOne(1L))
                .expectErrorMatches(e ->
                        e instanceof IllegalStateException &&
                                e.getMessage().contains("not available")
                )
                .verify();

        // save çağrısı yapılmamalı
        verify(repo, never()).save(any());
    }

    // Benzer şekilde returnOne için iki test yazın:
    @Test
    void returnOne_WhenBelowTotal_ShouldIncrement() {
        Book existing = Book.builder()
                .id(2L)
                .totalCopies(5)
                .availableCopies(3)
                .build();
        when(repo.findById(2L)).thenReturn(Mono.just(existing));
        when(repo.save(any(Book.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.returnOne(2L))
                .expectNextMatches(b -> b.getAvailableCopies() == 4)
                .verifyComplete();

        verify(repo).save(argThat(b -> b.getAvailableCopies() == 4));
    }

    @Test
    void returnOne_WhenAtMax_ShouldError() {
        Book existing = Book.builder()
                .id(3L)
                .totalCopies(5)
                .availableCopies(5)
                .build();
        when(repo.findById(3L)).thenReturn(Mono.just(existing));

        StepVerifier.create(service.returnOne(3L))
                .expectErrorMatches(e ->
                        e instanceof IllegalStateException &&
                                e.getMessage().contains("Cannot increment")
                )
                .verify();

        verify(repo, never()).save(any());
    }
}
