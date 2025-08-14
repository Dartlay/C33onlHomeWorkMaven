package com.book.controller.admin;

import com.book.dto.UserDTO;
import com.book.model.Author;
import com.book.model.Book;
import com.book.model.Genre;
import com.book.model.User;
import com.book.repository.UserRepository;
import com.book.service.BookService;
import com.book.service.UserService;
import com.book.service.AuthorService;
import com.book.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    private final BookService bookService;
    private final UserService userService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping
    public String adminPanel(Model model, Principal principal) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("users", userService.getAllUsers());

        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }

        return "admin/index";
    }

    @GetMapping("/books")
    public String booksList(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "admin/books/list";
    }

    @GetMapping("/books/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin/books/add";
    }

    @PostMapping("/books/add")
    public String addBook(
            @ModelAttribute Book book,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "cover", required = false) MultipartFile cover,
            @RequestParam("authorName") String authorName,
            @RequestParam("genreName") String genreName,
            Principal principal) throws IOException {

        // Обработка автора
        Author author = authorService.findOrCreateAuthor(authorName);
        book.setAuthor(author);

        // Обработка жанра
        Genre genre = genreService.findOrCreateGenre(genreName);
        book.setGenre(genre);

        User user = userService.getUserByUsername(principal.getName());
        bookService.saveBook(book, file, cover, user);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("authorName", book.getAuthor() != null ? book.getAuthor().getName() : "");
        model.addAttribute("genreName", book.getGenre() != null ? book.getGenre().getName() : "");
        return "admin/books/edit";
    }

    @PostMapping("/books/edit/{id}")
    public String editBook(
            @PathVariable Long id,
            @ModelAttribute Book book,
            @RequestParam("authorName") String authorName,
            @RequestParam("genreName") String genreName,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) MultipartFile cover) throws IOException {

        Book existingBook = bookService.findById(id);

        // Тут обновим автора
        Author author = authorService.findOrCreateAuthor(authorName);
        existingBook.setAuthor(author);

        // а тут обновим жанр
        Genre genre = genreService.findOrCreateGenre(genreName);
        existingBook.setGenre(genre);

        // Обновляем остальные поля
        existingBook.setTitle(book.getTitle());
        existingBook.setDescription(book.getDescription());
        existingBook.setYear(book.getYear());

        bookService.saveBook(existingBook, file, cover, existingBook.getUploadedBy());
        return "redirect:/admin/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }

    @GetMapping("/users")
    public String usersList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users/list";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUserForm(@PathVariable Long id, Model model, Principal principal) {
        UserDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("username", principal.getName());
        return "admin/users/delete";
    }

    // удаляем юзера
    @PostMapping("/users/delete/{id}")
    public String deleteUser(
            @PathVariable Long id,
            @RequestParam(required = false) boolean confirm,
            RedirectAttributes redirectAttributes) {

        if (!confirm) {
            redirectAttributes.addFlashAttribute("error",
                    "Deletion not confirmed");
            return "redirect:/admin/users/delete/" + id;
        }

        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success",
                    "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error deleting user: "
                            + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // меняем юзера
    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        UserDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/users/edit";
    }

    @PostMapping("/users/edit/{id}")
    public String editUser(
            @PathVariable Long id,
            @Valid @ModelAttribute("user") UserDTO userDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/users/edit";
        }

        try {
            userService.updateUser(id, userDTO);
            redirectAttributes.addFlashAttribute("success",
                    "User updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating user: "
                    + e.getMessage());
        }
        return "redirect:/admin/users";
    }

}