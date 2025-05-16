package com.example.demo.repository;

import com.example.demo.model.BorrowingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    Page<BorrowingRecord> findByUserId(Long userId, Pageable pageable);
    Page<BorrowingRecord> findByBookId(Long bookId, Pageable pageable);
    boolean existsByUserIdAndBookIdAndReturnDateAfter(Long userId, Long bookId, LocalDate borrowDate);
    boolean existsByBookIdAndReturnDateAfter(Long bookId, LocalDate borrowDate);
}