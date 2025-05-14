package com.mfy98.borrowservice.repository;

import com.mfy98.borrowservice.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findByUserId(Long userId);
    List<Borrowing> findByDueDateBefore(LocalDate date);
}
