package com.example.demo.validator;

import com.example.demo.model.Author;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.AuthorRepository;
import org.springframework.stereotype.Component;

@Component
public class AuthorValidator {

    private final AuthorRepository authorRepository;

    public AuthorValidator(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void validateAuthor(Author author) {
        if (author.getName() == null || author.getName().isBlank()) {
            throw new ValidationException("Author name is required");
        }
        if (author.getBirthDate() == null) {
            throw new ValidationException("Author birth date is required");
        }
        if (author.getNationality() == null || author.getNationality().isBlank()) {
            throw new ValidationException("Author nationality is required");
        }
    }

    public void validateUniqueAuthor(Author author) {
        boolean exists = authorRepository.existsByNameAndBirthDate(
                author.getName(), author.getBirthDate());
        if (exists) {
            throw new IllegalArgumentException("Author already exists with same name and birth date.");
        }
    }
}