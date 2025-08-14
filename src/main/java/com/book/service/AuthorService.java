package com.book.service;

import com.book.model.Author;
import com.book.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public Author findOrCreateAuthor(String name) {
        return authorRepository.findByName(name)
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(name);
                    return authorRepository.save(newAuthor);
                });
    }
}