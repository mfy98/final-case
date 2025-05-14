package com.mfy98.borrowservice.mapper;

import com.mfy98.borrowservice.dto.*;
import com.mfy98.borrowservice.entity.Borrowing;

public class BorrowingMapper {
    public static Borrowing toEntity(CreateBorrowingRequest req) {
        return Borrowing.builder()
                .bookId(req.getBookId())
                .userId(req.getUserId())
                .build();
    }

    public static BorrowingResponse toDto(Borrowing b) {
        return BorrowingResponse.builder()
                .id(b.getId())
                .bookId(b.getBookId())
                .userId(b.getUserId())
                .borrowDate(b.getBorrowDate())
                .dueDate(b.getDueDate())
                .returnDate(b.getReturnDate())
                .build();
    }
}