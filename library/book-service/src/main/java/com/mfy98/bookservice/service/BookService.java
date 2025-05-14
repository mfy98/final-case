package com.mfy98.bookservice.service;

import com.mfy98.bookservice.entity.Book;
import com.mfy98.bookservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repo;
    private final R2dbcEntityTemplate template;


    public Mono<Book> create(Book b) {
        b.setAvailableCopies(b.getTotalCopies());
        return repo.save(b);
    }

    public Mono<Book> getById(Long id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Book not found: " + id)));
    }

    public Mono<Book> update(Long id, Book b) {
        b.setId(id);
        return repo.save(b);
    }

    public Mono<Void> delete(Long id) {
        return repo.deleteById(id);
    }

    public Mono<Book> borrowOne(Long id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new IllegalStateException("Book not found: " + id)))
                .flatMap(b -> {
                    if (b.getAvailableCopies() > 0) {
                        b.setAvailableCopies(b.getAvailableCopies() - 1);
                        return repo.save(b);
                    }
                    return Mono.error(new IllegalStateException("Book not available: " + id));
                });
    }

    public Mono<Book> returnOne(Long id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new IllegalStateException("Book not found: " + id)))
                .flatMap(b -> {
                    if (b.getAvailableCopies() < b.getTotalCopies()) {
                        b.setAvailableCopies(b.getAvailableCopies() + 1);
                        return repo.save(b);
                    }
                    return Mono.error(new IllegalStateException("Cannot increment stock: " + id));
                });
    }

    public Flux<Book> streamAvailability(Long id) {
        return Flux.interval(Duration.ofSeconds(10))
                .flatMap(i -> repo.findById(id));
    }

    public Flux<Book> search(
            String title,
            String author,
            String isbn,
            String genre,
            int page,
            int size
    ) {
        Criteria criteria = null;

        if (title != null && !title.isBlank()) {
            criteria = Criteria.where("title").like("%" + title + "%");
        }
        if (author != null && !author.isBlank()) {
            Criteria c = Criteria.where("author").like("%" + author + "%");
            criteria = (criteria == null ? c : criteria.and(c));
        }
        if (isbn != null && !isbn.isBlank()) {
            Criteria c = Criteria.where("isbn").is(isbn);
            criteria = (criteria == null ? c : criteria.and(c));
        }
        if (genre != null && !genre.isBlank()) {
            Criteria c = Criteria.where("genre").like("%" + genre + "%");
            criteria = (criteria == null ? c : criteria.and(c));
        }

        Query query = (criteria == null
                ? Query.empty()
                : Query.query(criteria)
        ).sort(Sort.by("title"))
                .limit(size)
                .offset((long) page * size);

        return template.select(query, Book.class);
    }
}
