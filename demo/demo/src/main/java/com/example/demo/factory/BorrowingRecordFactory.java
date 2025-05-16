package com.example.demo.factory;

import com.example.demo.model.BorrowingRecord;
import com.example.demo.model.Book;
import com.example.demo.model.User;
import java.time.LocalDate;

public class BorrowingRecordFactory {
    // Factory method
    public static BorrowingRecord create(User user, Book book, LocalDate borrowDate, LocalDate returnDate) {
        BorrowingRecord record = new BorrowingRecord();
        record.setUser(user);
        record.setBook(book);
        record.setBorrowDate(borrowDate);
        record.setReturnDate(returnDate);
        return record;
    }
}