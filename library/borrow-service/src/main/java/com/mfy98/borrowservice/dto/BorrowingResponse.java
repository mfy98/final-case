package com.mfy98.borrowservice.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingResponse {
    private Long id;
    private Long bookId;
    private Long userId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
}