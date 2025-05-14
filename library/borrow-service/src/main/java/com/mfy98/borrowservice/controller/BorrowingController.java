package com.mfy98.borrowservice.controller;

import com.mfy98.borrowservice.dto.*;
import com.mfy98.borrowservice.exception.ResourceNotFoundException;
import com.mfy98.borrowservice.service.IBorrowingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowingController {

    private final IBorrowingService svc;
    public BorrowingController(IBorrowingService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<BorrowingResponse> borrow(
            @Valid @RequestBody CreateBorrowingRequest req) {
        BorrowingResponse resp = svc.borrow(req);
        return ResponseEntity
                .created(URI.create("/borrow/" + resp.getId()))
                .body(resp);
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<BorrowingResponse> returnBook(@PathVariable Long id) {
        BorrowingResponse resp = svc.returnBook(id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<BorrowingResponse>> history(@PathVariable Long userId) {
        List<BorrowingResponse> list = svc.history(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowingResponse>> overdue() {
        return ResponseEntity.ok(svc.overdue());
    }
}