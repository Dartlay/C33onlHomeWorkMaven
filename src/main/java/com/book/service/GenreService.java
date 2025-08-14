package com.book.service;

import com.book.model.Genre;
import com.book.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public Genre findOrCreateGenre(String name) {
        return genreRepository.findByName(name)
                .orElseGet(() -> {
                    Genre newGenre = new Genre();
                    newGenre.setName(name);
                    return genreRepository.save(newGenre);
                });
    }
}