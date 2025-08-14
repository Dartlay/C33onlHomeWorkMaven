package com.book.service;

import com.book.model.Book;
import com.book.model.Genre;
import com.book.model.User;
import com.book.repository.BookRepository;
import com.book.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final FileStorageService fileStorageService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Метод для поиска книг
    public List<Book> searchBooks(String query) {
        return bookRepository.searchBooks(query);
    }

    // Метод для фильтрации книг
    public List<Book> filterBooks(Long genreId) {
        return bookRepository.filterBooks(genreId);
    }

    // Получение всех жанров для фильтра
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }


    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    @Transactional
    public Book saveBook(Book book, MultipartFile file, MultipartFile cover, User user) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filePath = fileStorageService.store(file, uploadDir + "/books");
            book.setFilePath("books/" + filePath);
        }
        if (cover != null && !cover.isEmpty()) {
            String coverPath = fileStorageService.store(cover, uploadDir + "/covers");
            book.setCoverPath("covers/" + coverPath);
        }
        book.setUploadedBy(user);
        return bookRepository.save(book);
    }

    public List<Book> getNewestBooks() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        return bookRepository.findNewestBooks(cutoffDate);
    }

    public List<Book> getTop10Newest() {
        return bookRepository.findTop10Newest();
    }

    public List<Book> getRandom10() {
        return bookRepository.findRandom10();
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findById(id);
        try {
            if (book.getFilePath() != null) {
                fileStorageService.delete(uploadDir + "/" + book.getFilePath());
            }
            if (book.getCoverPath() != null) {
                fileStorageService.delete(uploadDir + "/" + book.getCoverPath());
            }
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
        bookRepository.delete(book);
    }
}
