package com.book.controller;

import com.book.model.Book;
import com.book.model.User;
import com.book.repository.UserRepository;
import com.book.security.JwtTokenProvider;
import com.book.service.BookService;
import com.book.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private LibraryService libraryService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultipartFile file;

    @Mock
    private MultipartFile cover;
    @Mock
    private User user;

    @Mock
    private Resource resource;

    @InjectMocks
    private BookController bookController;

    private Book testBook;
    private User testUser;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setFilePath("books/test.pdf");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
    }

    @Test

    public void saveBookWithValidFile_ShouldSuccess() throws IOException {

        Book book = new Book();
        Book savedBook = new Book();
        savedBook.setId(1L);
        when(file.isEmpty()).thenReturn(false);
        when(bookService.saveBook(any(), any(), any(), any())).thenReturn(savedBook);
        Book result = bookController.addBook(book, file, cover, user);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void saveBookWithoutFile_ShouldThrowException() {
        Book book = new Book();
        when(file.isEmpty()).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> {
            bookController.addBook(book, file, cover, user);
        });
    }

    @Test
    public void saveBookWithNullFile_ShouldThrowException() {
        Book book = new Book();
        assertThrows(IllegalArgumentException.class, () -> {
            bookController.addBook(book, null, cover, user);
        });
    }

    @Test
    public void saveBookWhenServiceThrowsIOException_ShouldPropagateException() throws IOException {
        Book book = new Book();
        when(file.isEmpty()).thenReturn(false);
        when(bookService.saveBook(any(), any(), any(), any()))
                .thenThrow(new IOException("File storage error"));
        assertThrows(IOException.class, () -> {
            bookController.addBook(book, file, cover, user);
        });
    }

    @Test
    void readBook_InvalidToken_ReturnsUnauthorized() throws Exception {
        String invalidToken = "invalidToken";
        when(tokenProvider.validateToken(invalidToken)).thenReturn(false);
        ResponseEntity<Resource> response = bookController.readBook(1L, 1, invalidToken);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(tokenProvider).validateToken(invalidToken);
        verifyNoMoreInteractions(tokenProvider);
        verifyNoInteractions(userRepository, bookService, libraryService);
    }
}
