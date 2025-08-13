package com.book.service;

import com.book.model.Book;
import com.book.model.User;
import com.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final FileStorageService fileStorageService;

    @Value("${file.upload-dir}")
    private String uploadDir;

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

    public List<Book> getTop10Books() {
        return bookRepository.findTop10ByFavorites();
    }


}
