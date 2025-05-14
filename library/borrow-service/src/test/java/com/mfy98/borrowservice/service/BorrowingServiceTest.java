package com.mfy98.borrowservice.service;

import com.mfy98.borrowservice.dto.CreateBorrowingRequest;
import com.mfy98.borrowservice.entity.Borrowing;
import com.mfy98.borrowservice.exception.ResourceNotFoundException;
import com.mfy98.borrowservice.repository.BorrowingRepository;
import com.mfy98.borrowservice.service.impl.BorrowingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BorrowingServiceTest {

    @Mock
    private BorrowingRepository repo;

    @Mock
    private RestTemplate rest;

    @InjectMocks
    private BorrowingServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        service.bookSrv = "http://book-service:8083/books";
    }

    @Test
    void borrow_WhenBookAvailable_ShouldSaveAndReturn() {
        CreateBorrowingRequest req = new CreateBorrowingRequest(1L, 2L);
        Borrowing saved = Borrowing.builder()
                .id(5L)
                .bookId(1L)
                .userId(2L)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusWeeks(2))
                .build();

        when(rest.patchForObject(contains("/1/decrement"), isNull(), eq(Void.class)))
                .thenReturn(null);

        when(repo.save(any(Borrowing.class))).thenReturn(saved);

        var resp = service.borrow(req);
        assertEquals(5L, resp.getId());
        verify(repo).save(any(Borrowing.class));
    }

    @Test
    void borrow_WhenBookNotAvailable_ShouldThrow() {
        when(rest.patchForObject(contains("/1/decrement"), isNull(), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(IllegalStateException.class,
                () -> service.borrow(new CreateBorrowingRequest(1L, 2L)));
    }

    @Test
    void returnBook_WhenExists_ShouldIncrementAndReturn() {
        Borrowing existing = Borrowing.builder().id(7L).bookId(1L).build();
        when(repo.findById(7L)).thenReturn(Optional.of(existing));

        when(rest.patchForObject(contains("/1/increment"), isNull(), eq(Void.class)))
                .thenReturn(null);

        when(repo.save(existing)).thenReturn(existing);

        var resp = service.returnBook(7L);
        assertNotNull(resp.getReturnDate());
        verify(repo).save(existing);
    }
}
