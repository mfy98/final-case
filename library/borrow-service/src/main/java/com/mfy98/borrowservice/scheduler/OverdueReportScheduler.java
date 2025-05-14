package com.mfy98.borrowservice.scheduler;

import com.mfy98.borrowservice.dto.BorrowingResponse;
import com.mfy98.borrowservice.service.IBorrowingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OverdueReportScheduler {

    private static final Logger log = LoggerFactory.getLogger(OverdueReportScheduler.class);
    private final IBorrowingService borrowingService;


    //  01:00’da çalışıyor

    @Scheduled(cron = "0 0 1 * * *")
    public void sendDailyOverdueReport() {
        List<BorrowingResponse> overdue = borrowingService.overdue();
        log.info("=== Daily Overdue Report: {} kayıt bulundu ===", overdue.size());
        overdue.forEach(b -> log.info(
                "Overdue[id={}, userId={}, bookId={}, dueDate={}]",
                b.getId(), b.getUserId(), b.getBookId(), b.getDueDate()
        ));
    }
}
