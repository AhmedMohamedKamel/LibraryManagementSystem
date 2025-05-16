package com.example.demo.service;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.validator.AuthorValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorValidator authorValidator;

    public AuthorService(AuthorRepository authorRepository, AuthorValidator authorValidator) {
        this.authorRepository = authorRepository;
        this.authorValidator = authorValidator;
    }

    public Page<Author> getAllAuthors(Pageable pageable) {
        System.out.println("info: Fetching all authors with pagination: " + pageable);
        Page<Author> authors = authorRepository.findAll(pageable);
        System.out.println("debug: Number of authors fetched: " + authors.getNumberOfElements());
        return authors;
    }

    public Author getAuthorById(Long id) {
        System.out.println("info: Fetching author with id: " + id);
        try {
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
            System.out.println("debug: Found author: " + author.getName());
            return author;
        } catch (ResourceNotFoundException e) {
            System.out.println("error: " + e.getMessage());
            throw e;
        }
    }

    public Author createAuthor(Author author) {
        System.out.println("info: Creating author: " + author.getName());
        authorValidator.validateAuthor(author);
        authorValidator.validateUniqueAuthor(author);
        Author savedAuthor = authorRepository.save(author);
        System.out.println("debug: Author created with id: " + savedAuthor.getId());
        return savedAuthor;
    }

    public Author updateAuthor(Long id, Author updatedAuthor) {
        System.out.println("info: Updating author with id: " + id);
        Author existing = getAuthorById(id);
        existing.setName(updatedAuthor.getName());
        existing.setBirthDate(updatedAuthor.getBirthDate());
        existing.setNationality(updatedAuthor.getNationality());
        authorValidator.validateAuthor(existing);
        Author updated = authorRepository.save(existing);
        System.out.println("debug: Author updated: " + updated.getName());
        return updated;
    }

    public void deleteAuthor(Long id) {
        System.out.println("info: Deleting author with id: " + id);
        if (!authorRepository.existsById(id)) {
            System.out.println("warn: Author with id " + id + " not found");
            throw new ResourceNotFoundException("Author not found");
        }
        authorRepository.deleteById(id);
        System.out.println("debug: Author with id " + id + " deleted");
    }
}
