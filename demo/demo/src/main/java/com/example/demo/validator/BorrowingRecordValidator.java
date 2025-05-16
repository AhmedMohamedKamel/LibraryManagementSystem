package com.example.demo.validator;

import com.example.demo.model.BorrowingRecord;
import com.example.demo.repository.BorrowingRecordRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BorrowingRecordValidator {

    private final BorrowingRecordRepository borrowingRecordRepository;

    public BorrowingRecordValidator(BorrowingRecordRepository borrowingRecordRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
    }

    public void validateBorrowingRecord(BorrowingRecord record) {
        if (record.getUser() == null || record.getBook() == null) {
            throw new IllegalArgumentException("User and Book must be provided.");
        }

        boolean exists = borrowingRecordRepository.existsByUserIdAndBookIdAndReturnDateAfter(
                record.getUser().getId(),
                record.getBook().getId(),
                record.getBorrowDate()
        );

        if (exists) {
            throw new IllegalArgumentException("This user has already borrowed this book and has not returned it yet.");
        }

        if (record.getBorrowDate() != null && record.getReturnDate() != null &&
                record.getReturnDate().isBefore(record.getBorrowDate())) {
            throw new IllegalArgumentException("Return date cannot be before borrow date.");
        }
    }

    public void validateBookNotAlreadyBorrowed(Long bookId, LocalDate borrowDate) {
        boolean isBorrowed = borrowingRecordRepository.existsByBookIdAndReturnDateAfter(bookId, borrowDate);

        if (isBorrowed) {
            throw new IllegalArgumentException("This book is already borrowed by another user and not yet returned.");
        }
    }
}
