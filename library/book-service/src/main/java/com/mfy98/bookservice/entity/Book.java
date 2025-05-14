package com.mfy98.bookservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("books")
public class Book {

    @Id
    @Column("id")
    private Long id;

    @Column("title")
    private String title;

    @Column("author")
    private String author;

    @Column("isbn")
    private String isbn;

    @Column("genre")
    private String genre;

    @Column("total_copies")
    private int totalCopies;

    @Column("available_copies")
    private int availableCopies;
}
