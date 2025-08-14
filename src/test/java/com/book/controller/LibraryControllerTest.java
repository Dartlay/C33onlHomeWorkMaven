package com.book.controller;

import com.book.model.Book;
import com.book.model.User;
import com.book.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryControllerTest {

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private LibraryController libraryController;

    private User testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
    }

    @Test
    void getUserLibrary_AuthenticatedUser_ReturnsBooks() {
        when(libraryService.getUserLibrary(testUser)).thenReturn(Collections.singletonList(testBook));

        ResponseEntity<List<Book>> response = libraryController.getUserLibrary(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getUserLibrary_UnauthenticatedUser_ReturnsUnauthorized() {
        ResponseEntity<List<Book>> response = libraryController.getUserLibrary(null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void addToLibrary_ValidBook_ReturnsOk() {
        ResponseEntity<String> response = libraryController.addToLibrary(1L, testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(libraryService).addToLibrary(1L, testUser);
    }

    @Test
    void removeFromLibrary_ValidBook_ReturnsOk() {
        ResponseEntity<String> response = libraryController.removeFromLibrary(1L, testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(libraryService).removeFromLibrary(1L, testUser);
    }

    @Test
    void updateReadingProgress_ValidPage_ReturnsOk() {
        ResponseEntity<String> response = libraryController.updateReadingProgress(1L, 50, testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(libraryService).updateReadingProgress(1L, testUser, 50);
    }
}