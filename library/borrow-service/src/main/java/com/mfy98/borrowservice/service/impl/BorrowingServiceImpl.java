package com.mfy98.borrowservice.service.impl;

import com.mfy98.borrowservice.dto.*;
import com.mfy98.borrowservice.entity.Borrowing;
import com.mfy98.borrowservice.exception.ResourceNotFoundException;
import com.mfy98.borrowservice.mapper.BorrowingMapper;
import com.mfy98.borrowservice.repository.BorrowingRepository;
import com.mfy98.borrowservice.service.IBorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BorrowingServiceImpl implements IBorrowingService {

    private final BorrowingRepository borrowingRepository;
    /** Book-Service URL’i application.yml’den inject edilecek */
    @Value("${book.service-url}")
    public String bookSrv;
    private final RestTemplate rest;
    @Override
    public BorrowingResponse borrow(CreateBorrowingRequest req) {
        try {
            rest.patchForObject(bookSrv + "/" + req.getBookId() + "/decrement",
                    null,
                    Void.class);
        } catch (HttpClientErrorException ex) {
            throw new IllegalStateException("Book not available: " + req.getBookId());
        }

        // İşleyip kaydetmeye devam
        Borrowing b = BorrowingMapper.toEntity(req);
        b.setBorrowDate(LocalDate.now());
        b.setDueDate(LocalDate.now().plusWeeks(2));
        Borrowing saved = borrowingRepository.save(b);
        return BorrowingMapper.toDto(saved);
    }

    @Override
    public BorrowingResponse returnBook(Long id) {
        Borrowing b = borrowingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found: " + id));
        b.setReturnDate(LocalDate.now());
        Borrowing updated = borrowingRepository.save(b);
        return BorrowingMapper.toDto(updated);
    }

    @Override
    public List<BorrowingResponse> history(Long userId) {
        return borrowingRepository.findByUserId(userId).stream()
                .map(BorrowingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowingResponse> overdue() {
        return borrowingRepository.findByDueDateBefore(LocalDate.now()).stream()
                .map(BorrowingMapper::toDto)
                .collect(Collectors.toList());
    }
}