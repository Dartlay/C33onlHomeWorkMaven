package com.book.controller;

import com.book.model.Book;
import com.book.model.User;
import com.book.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @GetMapping
    public ResponseEntity<List<Book>> getUserLibrary(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(libraryService.getUserLibrary(user));
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Void> addToLibrary(
            @PathVariable Long bookId,
            @AuthenticationPrincipal User user) {
        libraryService.addToLibrary(bookId, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> removeFromLibrary(
            @PathVariable Long bookId,
            @AuthenticationPrincipal User user) {
        libraryService.removeFromLibrary(bookId, user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{bookId}/progress")
    public ResponseEntity<Void> updateReadingProgress(
            @PathVariable Long bookId,
            @RequestParam int page,
            @AuthenticationPrincipal User user) {
        libraryService.updateReadingProgress(bookId, user, page);
        return ResponseEntity.ok().build();
    }
}