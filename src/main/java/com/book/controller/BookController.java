package com.book.controller;

import com.book.model.Book;
import com.book.model.User;
import com.book.repository.UserRepository;
import com.book.security.JwtTokenProvider;
import com.book.service.BookService;
import com.book.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;
    private final LibraryService libraryService;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;


    @Value("${file.upload-dir}")
    private String uploadDir; // Конфиг

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}/read")
    public ResponseEntity<Resource> readBook(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam String token) throws IOException {

        // чекаем токен
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // получаем юзера из токена
        String username = tokenProvider.getUsernameFromJWT(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookService.findById(id);

        try {
            libraryService.updateReadingProgress(id, user, page);
        } catch (Exception e) {
            log.warn("Не удалось обновить прогресс чтения для пользователя {} и книги {}",
                    user.getUsername(), id, e);
        }

        Path filePath = Paths.get(uploadDir).resolve(book.getFilePath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            log.error("Файл книги не найден: {}", book.getFilePath());
            return ResponseEntity.notFound().build();
        }

        String contentType = determineContentType(book.getFilePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }


    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "epub":
                return "application/epub+zip";
            case "mobi":
                return "application/x-mobipocket-ebook";
            default:
                return "application/octet-stream";
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadBook(
            @PathVariable Long id,
            @RequestParam String token) throws IOException {


        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        Book book = bookService.findById(id);


        Path filePath = Paths.get(uploadDir).resolve(book.getFilePath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());


        if (!resource.exists()) {
            throw new RuntimeException("File not found " + book.getFilePath());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/{id}/cover")
    public ResponseEntity<Resource> getCoverImage(@PathVariable Long id) throws IOException {
        Book book = bookService.findById(id);
        if (book.getCoverPath() == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(uploadDir).resolve(book.getCoverPath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) //Картинка от формата
                .body(resource);
    }

    // не забудь сделать
    @GetMapping("/top")
    public ResponseEntity<List<Book>> getTop10Books() {
        return ResponseEntity.ok(bookService.getRandom10());
    }

    @GetMapping("/newest")
    public ResponseEntity<List<Book>> getNewestBooks() {
        return ResponseEntity.ok(bookService.getTop10Newest());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Book addBook(
            @RequestPart Book book,
            @RequestPart MultipartFile file,
            @RequestPart(required = false) MultipartFile cover,
            @AuthenticationPrincipal User user) throws IOException {

        if (file == null) {
            throw new IllegalArgumentException("Book file is required");
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Book file cannot be empty");
        }

        try {
            return bookService.saveBook(book, file, cover, user);
        } catch
        (IOException e) {
            log.error("Failed to save book file", e);
            throw e;
        }
    }
}