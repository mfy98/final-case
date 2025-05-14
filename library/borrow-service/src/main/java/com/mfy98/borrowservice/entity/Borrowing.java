package com.mfy98.borrowservice.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Data
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "borrowings")
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;
    private Long userId;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

}