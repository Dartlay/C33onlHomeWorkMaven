package com.book.controller;

import com.book.model.Book;

import com.book.model.User;
import com.book.repository.UserRepository;
import com.book.security.JwtTokenProvider;
import com.book.service.AuthService;
import com.book.service.BookService;
import com.book.service.LibraryService;
import lombok.RequiredArgsConstructor;
import com.book.dto.AuthResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final BookService bookService;
    private final LibraryService libraryService;
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    private static final String AUTH_API_URL = "http://localhost:8080/api/auth";

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
        }
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/register-success")
    public String registerSuccess() {
        return "register-success";
    }

    @GetMapping("/login-success")
    public String loginSuccess(HttpSession session) {
        String role = (String) session.getAttribute("role");

        // Проверяем роль с учётом возможных вариантов (ROLE_ADMIN или ADMIN)
        if (role != null && (role.equals("ROLE_ADMIN") || role.equals("ADMIN"))) {
            return "redirect:/admin";
        } else {
            return "redirect:/book"; // Все остальные пользователи идут на /book
        }
    }

    @GetMapping("/book")
    public String book(
            HttpSession session,
            Model model,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "uploadedAt") String sortBy) {

        String token = (String) session.getAttribute("token");
        boolean isAuthenticated = token != null && tokenProvider.validateToken(token);
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            String username = tokenProvider.getUsernameFromJWT(token);
            model.addAttribute("username", username);
        }

        List<Book> books;
        if (search != null && !search.isEmpty()) {
            books = bookService.searchBooks(search);
        } else if (genreId != null || startDate != null || endDate != null) {
            books = bookService.filterBooks(genreId);
        } else {
            books = bookService.findAll();
        }

        model.addAttribute("books", books);
        model.addAttribute("genres", bookService.getAllGenres());
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedGenreId", genreId);

        if (!model.containsAttribute("success")) {
            model.addAttribute("success", "");
        }
        if (!model.containsAttribute("error")) {
            model.addAttribute("error", "");
        }

        return "book";
    }

    @GetMapping("/library")
    public String libraryPage(HttpSession session, Model model) {


        String token = (String) session.getAttribute("token");
        if (token == null || !tokenProvider.validateToken(token)) {
            return "redirect:/login";
        }

        // Получаем пользователя
        User user = userRepository.findByUsername(tokenProvider.getUsernameFromJWT(token))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Получаем книги из библиотеки
        List<Book> libraryBooks = libraryService.getUserLibrary(user);

        // Добавляем атрибуты в модель
        model.addAttribute("books", libraryBooks);
        model.addAttribute("isAuthenticated", true);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("token", token); // Добавляем токен в модель

        return "library";
    }

    @PostMapping("/add-to-library")
    public String addToLibrary(
            @RequestParam Long bookId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String token = (String) session.getAttribute("token");
        if (token == null || !tokenProvider.validateToken(token)) {
            return "redirect:/login";
        }

        try {
            User user = userRepository.findByUsername(tokenProvider.getUsernameFromJWT(token))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            libraryService.addToLibrary(bookId, user);
            redirectAttributes.addFlashAttribute("success", "Книга добавлена в библиотеку");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }

        return "redirect:/book";
    }

    @PostMapping("/remove-from-library")
    public String removeFromLibrary(
            @RequestParam Long bookId,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        // Проверяем, была ли отправлена форма с подтверждением
        String confirmParam = request.getParameter("confirm");
        if (confirmParam == null || !confirmParam.equals("true")) {
            redirectAttributes.addFlashAttribute("error", "Подтверждение не получено");
            return "redirect:/library";
        }

        try {
            String token = (String) session.getAttribute("token");
            if (token == null || !tokenProvider.validateToken(token)) {
                redirectAttributes.addFlashAttribute("error", "Требуется авторизация");
                return "redirect:/login";
            }

            User user = userRepository.findByUsername(tokenProvider.getUsernameFromJWT(token))
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            libraryService.removeFromLibrary(bookId, user);
            redirectAttributes.addFlashAttribute("success", "Книга успешно удалена из библиотеки");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/library";
    }

    @GetMapping("/top10")
    public String top10Page(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        boolean isAuthenticated = token != null && tokenProvider.validateToken(token);

        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            String username = tokenProvider.getUsernameFromJWT(token);
            model.addAttribute("username", username);
        }

        List<Book> topBooks = bookService.getRandom10(); // или bookService.getTop10ByRating()
        model.addAttribute("books", topBooks);

        return "top10";
    }

    @GetMapping("/newest")
    public String newestPage(HttpSession session, Model model) {
        // Проверка аутентификации
        String token = (String) session.getAttribute("token");
        boolean isAuthenticated = token != null && tokenProvider.validateToken(token);
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            model.addAttribute("username", tokenProvider.getUsernameFromJWT(token));
        }

        // Получаем 10 самых новых книг
        List<Book> newestBooks = bookService.getTop10Newest();
        model.addAttribute("books", newestBooks);

        return "newest";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            AuthResponse authResponse = authService.login(username, password);

            // 1. Сохраняем в сессию
            HttpSession session = request.getSession();
            session.setAttribute("token", authResponse.getToken());
            session.setAttribute("username", authResponse.getUsername());
            session.setAttribute("role", authResponse.getRole());

            // 2. Устанавливаем куку
            Cookie jwtCookie = new Cookie("jwtToken", authResponse.getToken());
            jwtCookie.setPath("/");
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7 дней
            response.addCookie(jwtCookie);

            // 3. Устанавливаем аутентификацию в SecurityContext
            Authentication authentication = tokenProvider.getAuthentication(authResponse.getToken());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (authResponse.getRole().equals("ROLE_ADMIN")) {
                return "redirect:/admin";
            }
            return "redirect:/book";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Очищаем аутентификацию
        SecurityContextHolder.clearContext();

        // Удаляем сессию
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Удаляем cookie
        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return "redirect:/book";
    }
}