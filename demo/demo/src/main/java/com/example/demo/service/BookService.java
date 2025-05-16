package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.validator.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookValidator bookValidator;
    private Author author;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository,BookValidator bookValidator) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookValidator = bookValidator;
    }

    public Page<Book> getAllBooks(Pageable pageable) {
        System.out.println("info: Fetching all books with pagination: " + pageable);
        Page<Book> books = bookRepository.findAll(pageable);
        System.out.println("debug: Number of books fetched: " + books.getNumberOfElements());
        return books;
    }

    public Book getBookById(Long id) {
        System.out.println("info: Fetching book with id: " + id);
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
            System.out.println("debug: Found book: " + book.getTitle());
            return book;
        } catch (ResourceNotFoundException e) {
            System.out.println("error: " + e.getMessage());
            throw e;
        }
    }

    public Book createBook(Book book) {
        System.out.println("info: Creating book: " + book.getTitle());
        bookValidator.validateUniqueBook(book);

        Long authorId = book.getAuthor() != null ? book.getAuthor().getId() : null;
        if (authorId == null) {
            System.out.println("error: Author must be provided.");
            throw new IllegalArgumentException("Author must be provided.");
        }

        Author author;
        try {
            author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
        } catch (ResourceNotFoundException e) {
            System.out.println("error: " + e.getMessage());
            throw e;
        }

        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        System.out.println("debug: Book created with id: " + savedBook.getId());
        return savedBook;
    }


    public Book updateBook(Long id, Book updatedBook) {
        System.out.println("info: Updating book with id: " + id);
        Book existingBook = getBookById(id);
        validateAuthorExists(updatedBook.getAuthor().getId());

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setPublicationDate(updatedBook.getPublicationDate());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setAvailable(updatedBook.isAvailable());
        existingBook.setAuthor(updatedBook.getAuthor());

        Book updated = bookRepository.save(existingBook);
        System.out.println("debug: Book updated: " + updated.getTitle());
        return updated;
    }

    public void deleteBook(Long id) {
        System.out.println("info: Deleting book with id: " + id);
        Book book = getBookById(id);
        bookRepository.delete(book);
        System.out.println("debug: Book with id " + id + " deleted");
    }

    public Page<Book> searchByTitle(String title, Pageable pageable) {
        System.out.println("info: Searching books by title containing: '" + title + "', pageable: " + pageable);
        Page<Book> books = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        System.out.println("debug: Found " + books.getNumberOfElements() + " books matching title.");
        return books;
    }

    public Page<Book> searchByAuthorName(String authorName, Pageable pageable) {
        System.out.println("info: Searching books by author name: '" + authorName + "', pageable: " + pageable);
        Page<Book> books = bookRepository.findByAuthorName(authorName, pageable);
        System.out.println("debug: Found " + books.getNumberOfElements() + " books matching author.");
        return books;
    }

    public Page<Book> searchByIsbn(String isbn, Pageable pageable) {
        System.out.println("info: Searching books by ISBN: '" + isbn + "', pageable: " + pageable);
        Page<Book> books = bookRepository.findByIsbn(isbn, pageable);
        System.out.println("debug: Found " + books.getNumberOfElements() + " books matching ISBN.");
        return books;
    }

    private void validateAuthorExists(Long authorId) {
        System.out.println("info: Validating existence of author with id: " + authorId);
        if (!authorRepository.existsById(authorId)) {
            System.out.println("error: Author not found with id " + authorId);
            throw new ResourceNotFoundException("Author not found with id " + authorId);
        }
        System.out.println("debug: Author with id " + authorId + " exists");
    }
}