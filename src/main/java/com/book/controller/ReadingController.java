package com.book.controller;

import com.book.model.Book;
import com.book.model.User;
import com.book.repository.UserRepository;
import com.book.security.JwtTokenProvider;
import com.book.service.BookService;
import com.book.service.LibraryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/read")
@RequiredArgsConstructor
public class ReadingController {

    private static final Logger log = LoggerFactory.getLogger(ReadingController.class);
    private final BookService bookService;
    private final LibraryService libraryService;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir; //выгрузка

    @GetMapping("/{id}")
    public String readBook(
            @PathVariable Long id,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // Проверка через сессию
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("token") == null) {
                redirectAttributes.addAttribute("redirect", "/read/" + id);
                return "redirect:/login";
            }

            // чек токена
            String token = (String) session.getAttribute("token");
            if (!tokenProvider.validateToken(token)) {
                return "redirect:/login";
            }

            // Получаем юзера
            String username = tokenProvider.getUsernameFromJWT(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Book book = bookService.findById(id);
            model.addAttribute("book", book);
            model.addAttribute("bookTitle", book.getTitle());

            // тип файла
            String filePath = book.getFilePath();
            if (filePath == null) {
                throw new IOException("Файл книги не найден");
            }

            String lowerCasePath = filePath.toLowerCase();

            if (lowerCasePath.endsWith(".txt")) {
                // Обработка тхт файлов
                Path fullPath = Paths.get(uploadDir).resolve(filePath).normalize();
                String content = Files.readString(fullPath, StandardCharsets.UTF_8);
                model.addAttribute("isTxt", true);
                model.addAttribute("textContent", content);
                return "text-reader";
                // других форматы
            } else {

                model.addAttribute("isTxt", false);
                return "text-reader"; //формат не тот
            }
            // ошибки
        } catch (IOException e) {
            log.error("Ошибка чтения файла книги ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при загрузке файла книги");
            return "redirect:/book";
        } catch (Exception e) {
            log.error("Ошибка при открытии книги ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка: " + e.getMessage());
            return "redirect:/book";
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadBook(
            @PathVariable Long id,
            @RequestParam String token) throws IOException {

        // чекаем токена
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Book book = bookService.findById(id);
        Path filePath = Paths.get(uploadDir).resolve(book.getFilePath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<Resource> getBookContent(
            @PathVariable Long id,
            @RequestParam String token) throws IOException {

        // чекаем токена
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Book book = bookService.findById(id);
        Path filePath = Paths.get(uploadDir).resolve(book.getFilePath()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
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
            case "txt":
                return "text/plain";
            default:
                return "application/octet-stream";
        }
    }
}