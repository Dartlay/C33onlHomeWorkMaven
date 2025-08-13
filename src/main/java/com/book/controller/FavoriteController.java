package com.book.controller;

import com.book.model.Book;
import com.book.model.User;
import com.book.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<List<Book>> getUserFavorites(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(favoriteService.getUserFavorites(user));
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Void> addFavorite(
            @PathVariable Long bookId,
            @AuthenticationPrincipal User user) {
        favoriteService.addFavorite(bookId, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long bookId,
            @AuthenticationPrincipal User user) {
        favoriteService.removeFavorite(bookId, user);
        return ResponseEntity.ok().build();
    }
}