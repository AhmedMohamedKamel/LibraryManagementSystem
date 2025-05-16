package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.factory.BorrowingRecordFactory;
import com.example.demo.model.BorrowingRecord;
import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.repository.BorrowingRecordRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.validator.BorrowingRecordValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowingRecordValidator borrowingRecordValidator;

    public BorrowingRecordService(BorrowingRecordRepository borrowingRecordRepository,
                                  UserRepository userRepository,
                                  BookRepository bookRepository,
                                  BorrowingRecordValidator borrowingRecordValidator) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.borrowingRecordValidator = borrowingRecordValidator;
    }

    public Page<BorrowingRecord> getAllRecords(Pageable pageable) {
        System.out.println("info: Fetching all borrowing records with pagination: " + pageable);
        Page<BorrowingRecord> records = borrowingRecordRepository.findAll(pageable);
        System.out.println("debug: Number of borrowing records fetched: " + records.getNumberOfElements());
        return records;
    }

    public BorrowingRecord getRecordById(Long id) {
        System.out.println("info: Fetching borrowing record with id: " + id);
        try {
            BorrowingRecord record = borrowingRecordRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
            System.out.println("debug: Found borrowing record for user id: " + record.getUser().getId() + ", book id: " + record.getBook().getId());
            return record;
        } catch (ResourceNotFoundException e) {
            System.out.println("error: " + e.getMessage());
            throw e;
        }
    }

    public BorrowingRecord createRecord(Long userId, Long bookId, LocalDate borrowDate, LocalDate returnDate) {
        System.out.println("info: Creating borrowing record for user id: " + userId + ", book id: " + bookId);
        User user;
        Book book;
        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
            book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        } catch (ResourceNotFoundException e) {
            System.out.println("error: " + e.getMessage());
            throw e;
        }

        BorrowingRecord record = BorrowingRecordFactory.create(user, book, borrowDate, returnDate);
        borrowingRecordValidator.validateBorrowingRecord(record);
        borrowingRecordValidator.validateBookNotAlreadyBorrowed(bookId, borrowDate);

        BorrowingRecord savedRecord = borrowingRecordRepository.save(record);
        System.out.println("debug: Borrowing record created with id: " + savedRecord.getId());
        return savedRecord;
    }

    public BorrowingRecord updateRecord(Long id, LocalDate borrowDate, LocalDate returnDate) {
        System.out.println("info: Updating borrowing record with id: " + id);
        BorrowingRecord record = getRecordById(id);
        record.setBorrowDate(borrowDate);
        record.setReturnDate(returnDate);
        BorrowingRecord updatedRecord = borrowingRecordRepository.save(record);
        System.out.println("debug: Borrowing record updated with new borrowDate: " + borrowDate + " and returnDate: " + returnDate);
        return updatedRecord;
    }

    public void deleteRecord(Long id) {
        System.out.println("info: Deleting borrowing record with id: " + id);
        BorrowingRecord record = getRecordById(id);
        borrowingRecordRepository.delete(record);
        System.out.println("debug: Borrowing record with id " + id + " deleted");
    }

    public Page<BorrowingRecord> getRecordsByUserId(Long userId, Pageable pageable) {
        System.out.println("info: Fetching borrowing records for user id: " + userId + " with pagination: " + pageable);
        Page<BorrowingRecord> records = borrowingRecordRepository.findByUserId(userId, pageable);
        System.out.println("debug: Number of borrowing records found for user: " + records.getNumberOfElements());
        return records;
    }

    public Page<BorrowingRecord> getRecordsByBookId(Long bookId, Pageable pageable) {
        System.out.println("info: Fetching borrowing records for book id: " + bookId + " with pagination: " + pageable);
        Page<BorrowingRecord> records = borrowingRecordRepository.findByBookId(bookId, pageable);
        System.out.println("debug: Number of borrowing records found for book: " + records.getNumberOfElements());
        return records;
    }
}