package com.book.service;

import com.book.model.Book;
import com.book.model.Favorite;
import com.book.model.User;
import com.book.repository.BookRepository;
import com.book.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final BookRepository bookRepository;

    public List<Book> getUserFavorites(User user) {
        return favoriteRepository.findByUser(user).stream()
                .map(Favorite::getBook)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addFavorite(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (favoriteRepository.existsByUserAndBook(user, book)) {
            throw new RuntimeException("Book already in favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setBook(book);
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        favoriteRepository.deleteByUserAndBook(user, book);
    }
}