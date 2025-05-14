package com.mfy98.borrowservice.service;

import com.mfy98.borrowservice.dto.BorrowingResponse;
import com.mfy98.borrowservice.dto.CreateBorrowingRequest;
import com.mfy98.borrowservice.repository.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface IBorrowingService {

    BorrowingResponse borrow(CreateBorrowingRequest req);
    BorrowingResponse returnBook(Long id);
    List<BorrowingResponse> history(Long userId);
    List<BorrowingResponse> overdue();
}