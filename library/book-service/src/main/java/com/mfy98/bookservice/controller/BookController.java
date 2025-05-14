package com.mfy98.bookservice.controller;

import com.mfy98.bookservice.entity.Book;
import com.mfy98.bookservice.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService svc;

    @PostMapping
    public Mono<ResponseEntity<Book>> create(@Valid @RequestBody Book b) {
        log.info("Received request to create book: {}", b);
        return svc.create(b)
                .doOnSuccess(saved -> log.info("Book created with id={}", saved.getId()))
                .doOnError(err -> log.error("Error creating book", err))
                .map(saved ->
                        ResponseEntity
                                .created(URI.create("/books/" + saved.getId()))
                                .body(saved)
                );
    }

    @GetMapping("/{id}")
    public Mono<Book> getById(@PathVariable Long id) {
        log.info("Fetching book by id={}", id);
        return svc.getById(id)
                .doOnNext(book -> log.info("Found book: {}", book))
                .doOnError(err -> log.error("Error fetching book id={}", id, err));
    }

    @GetMapping
    public Flux<Book> find(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Searching books with title={}, author={}, isbn={}, genre={}, page={}, size={}",
                title, author, isbn, genre, page, size);
        return svc.search(title, author, isbn, genre, page, size)
                .doOnNext(book -> log.debug("Matched book: {}", book))
                .doOnComplete(() -> log.info("Search complete"));
    }

    @PutMapping("/{id}")
    public Mono<Book> update(
            @PathVariable Long id,
            @Valid @RequestBody Book b
    ) {
        log.info("Updating book id={} with data={}", id, b);
        return svc.update(id, b)
                .doOnSuccess(book -> log.info("Book updated: {}", book))
                .doOnError(err -> log.error("Error updating book id={}", id, err));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        log.info("Deleting book id={}", id);
        return svc.delete(id)
                .doOnSuccess(v -> log.info("Book deleted id={}", id))
                .doOnError(err -> log.error("Error deleting book id={}", id, err));
    }

    @PatchMapping("/{id}/decrement")
    public Mono<Book> decrement(@PathVariable Long id) {
        log.info("Decrementing availableCopies for book id={}", id);
        return svc.borrowOne(id)
                .doOnSuccess(book -> log.info("Decrement complete, book now: {}", book))
                .doOnError(err -> log.error("Error decrementing book id={}", id, err));
    }

    @PatchMapping("/{id}/increment")
    public Mono<Book> increment(@PathVariable Long id) {
        log.info("Incrementing availableCopies for book id={}", id);
        return svc.returnOne(id)
                .doOnSuccess(book -> log.info("Increment complete, book now: {}", book))
                .doOnError(err -> log.error("Error incrementing book id={}", id, err));
    }

    @GetMapping(value = "/{id}/availability/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Book>> stream(@PathVariable Long id) {
        log.info("Starting availability stream for book id={}", id);
        return svc.streamAvailability(id)
                .doOnNext(book -> log.debug("Streamed availability: {}", book))
                .doOnError(err -> log.error("Error in availability stream for id={}", id, err))
                .map(book ->
                        ServerSentEvent.<Book>builder()
                                .data(book)
                                .build()
                );
    }
}
