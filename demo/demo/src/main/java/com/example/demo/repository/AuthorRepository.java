package com.example.demo.repository;

import com.example.demo.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;


public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByNameAndBirthDate(String name, LocalDate birthDate);
}