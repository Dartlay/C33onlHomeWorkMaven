package com.book.service;

import com.book.model.Book;
import com.book.model.Genre;
import com.book.model.User;
import com.book.repository.BookRepository;
import com.book.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private User testUser;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
    }

    @Test
    void findAll_ReturnsAllBooks() {
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(testBook));

        List<Book> books = bookService.findAll();

        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getTitle());
    }

    @Test
    void findById_ExistingId_ReturnsBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        Book book = bookService.findById(1L);

        assertEquals("Test Book", book.getTitle());
    }

    @Test
    void saveBook_WithFiles_SavesBook() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());
        MultipartFile cover = new MockMultipartFile("cover", "cover.jpg", "image/jpeg", "content".getBytes());

        when(fileStorageService.store(any(), any())).thenReturn("stored_file");
        when(bookRepository.save(any())).thenReturn(testBook);

        Book savedBook = bookService.saveBook(testBook, file, cover, testUser);

        assertNotNull(savedBook);
        verify(fileStorageService, times(2)).store(any(), any());
    }

    @Test
    void getTop10Newest_ReturnsNewestBooks() {
        when(bookRepository.findTop10Newest()).thenReturn(Collections.singletonList(testBook));

        List<Book> books = bookService.getTop10Newest();

        assertEquals(1, books.size());
    }

    @Test
    void deleteBook_ExistingBook_DeletesBook() throws IOException {
        testBook.setFilePath("path/to/file");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        bookService.deleteBook(1L);

        verify(bookRepository).delete(testBook);
        verify(fileStorageService).delete(any());
    }
}