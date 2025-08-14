package com.book.service;

import com.book.model.Book;
import com.book.model.User;
import com.book.model.UserLibrary;
import com.book.repository.BookRepository;
import com.book.repository.UserLibraryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private UserLibraryRepository libraryRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LibraryService libraryService;

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
    void getUserLibrary_ReturnsUserBooks() {
        UserLibrary libraryEntry = new UserLibrary();
        libraryEntry.setUser(testUser);
        libraryEntry.setBook(testBook);

        when(libraryRepository.findByUser(testUser)).thenReturn(Collections.singletonList(libraryEntry));

        List<Book> books = libraryService.getUserLibrary(testUser);

        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getTitle());
    }

    @Test
    void addToLibrary_NewBook_AddsToLibrary() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(libraryRepository.existsByUserAndBook(testUser, testBook)).thenReturn(false);

        libraryService.addToLibrary(1L, testUser);

        verify(libraryRepository).save(any(UserLibrary.class));
    }

    @Test
    void addToLibrary_ExistingBook_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(libraryRepository.existsByUserAndBook(testUser, testBook)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> libraryService.addToLibrary(1L, testUser));
    }

    @Test
    void removeFromLibrary_ExistingBook_RemovesFromLibrary() {
        UserLibrary libraryEntry = new UserLibrary();
        libraryEntry.setUser(testUser);
        libraryEntry.setBook(testBook);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(libraryRepository.findByUserAndBook(testUser, testBook)).thenReturn(Optional.of(libraryEntry));

        libraryService.removeFromLibrary(1L, testUser);

        verify(libraryRepository).delete(libraryEntry);
    }

    @Test
    void updateReadingProgress_ValidPage_UpdatesProgress() {
        UserLibrary libraryEntry = new UserLibrary();
        libraryEntry.setUser(testUser);
        libraryEntry.setBook(testBook);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(libraryRepository.findByUserAndBook(testUser, testBook)).thenReturn(Optional.of(libraryEntry));

        libraryService.updateReadingProgress(1L, testUser, 50);

        assertEquals(50, libraryEntry.getLastReadPage());
        verify(libraryRepository).save(libraryEntry);
    }
}

