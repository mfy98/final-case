package com.mfy98.borrowservice.controller;

import com.mfy98.borrowservice.dto.BorrowingResponse;
import com.mfy98.borrowservice.service.IBorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final IBorrowingService borrowingService;

    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowingResponse>> getOverdueReport() {
        List<BorrowingResponse> list = borrowingService.overdue();
        return ResponseEntity.ok(list);
    }
}
