package com.book.controller;

import com.book.model.Book;
import com.book.model.User;
import com.book.repository.UserRepository;
import com.book.security.JwtTokenProvider;
import com.book.service.AuthService;
import com.book.service.BookService;
import com.book.service.LibraryService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private LibraryService libraryService;

    @Mock
    private AuthService authService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private WebController webController;

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
    void bookPage_Authenticated_ReturnsView() {
        when(session.getAttribute("token")).thenReturn("validToken");
        when(tokenProvider.validateToken(anyString())).thenReturn(true);
        when(tokenProvider.getUsernameFromJWT(anyString())).thenReturn("testuser");
        when(bookService.findAll()).thenReturn(Collections.singletonList(testBook));

        String viewName = webController.book(session, model, null, null, null, null, null);

        assertEquals("book", viewName);
        verify(model).addAttribute("books", Collections.singletonList(testBook));
    }


}