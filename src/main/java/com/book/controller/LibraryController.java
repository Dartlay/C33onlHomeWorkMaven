package com.book.controller;

import com.book.model.Book;
import com.book.model.User;
import com.book.repository.UserRepository;
import com.book.security.JwtTokenProvider;
import com.book.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
@Tag(name = "User Library", description = "API for managing user's book library")
public class LibraryController {
    private final LibraryService libraryService;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    @GetMapping
    @Operation(summary = "Get user's library",
            description = "Retrieve all books in the user's library")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved library")
    public ResponseEntity<List<Book>> getUserLibrary(
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null) {
            log.warn("Unauthorized access attempt to user library");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Retrieving library for user: {}", user.getUsername());
        List<Book> library = libraryService.getUserLibrary(user);
        return ResponseEntity.ok(library);
    }

    @PostMapping("/{bookId}")
    @Operation(summary = "Add book to library",
            description = "Add a book to the user's library")
    @ApiResponse(responseCode = "200", description = "Book added successfully")
    @ApiResponse(responseCode = "400", description = "Book already in library or not found")
    public ResponseEntity<String> addToLibrary(
            @Parameter(description = "ID of the book to add") @PathVariable Long bookId,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            libraryService.addToLibrary(bookId, user);
            log.info("Book {} added to library for user {}", bookId, user.getUsername());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.warn("Failed to add book {} to library: {}", bookId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/remove-from-library")
    public String removeFromLibrary(
            @RequestParam Long bookId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            String token = (String) session.getAttribute("token");
            User user = userRepository.findByUsername(tokenProvider.getUsernameFromJWT(token))
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            libraryService.removeFromLibrary(bookId, user);
            redirectAttributes.addFlashAttribute("success", "Книга успешно удалена из библиотеки");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Произошла ошибка при удалении");
        }

        return "redirect:/library";
    }

    @DeleteMapping("/{bookId}")
    @Operation(summary = "Remove book from library")
    public ResponseEntity<String> removeFromLibrary(
            @PathVariable Long bookId,
            @AuthenticationPrincipal User user) {
        try {
            libraryService.removeFromLibrary(bookId, user);
            return ResponseEntity.ok("Книга успешно удалена из библиотеки");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{bookId}/progress")
    @Operation(summary = "Update reading progress",
            description = "Update the last read page for a book in the library")
    @ApiResponse(responseCode = "200", description = "Progress updated successfully")
    @ApiResponse(responseCode = "400", description = "Book not found in library")
    public ResponseEntity<String> updateReadingProgress(
            @Parameter(description = "ID of the book") @PathVariable Long bookId,
            @Parameter(description = "Page number") @RequestParam int page,
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            libraryService.updateReadingProgress(bookId, user, page);
            log.info("Updated reading progress for book {} to page {} for user {}",
                    bookId, page, user.getUsername());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.warn("Failed to update progress for book {}: {}", bookId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}