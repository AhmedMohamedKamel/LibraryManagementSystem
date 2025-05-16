package com.example.demo.validator;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Component;

@Component
public class BookValidator {
    private final BookRepository bookRepository;

    public BookValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void validateUniqueBook(Book book) {
        boolean exists = bookRepository.existsByTitleAndIsbn(
                book.getTitle(), book.getIsbn());
        if (exists) {
            throw new IllegalArgumentException("Book already exists with same title and ISBN.");
        }
    }
}
