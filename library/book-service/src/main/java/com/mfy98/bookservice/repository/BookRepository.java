package com.mfy98.bookservice.repository;

import com.mfy98.bookservice.entity.Book;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface BookRepository extends ReactiveCrudRepository<Book, Long> {


}
