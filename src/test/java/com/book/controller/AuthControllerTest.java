package com.book.controller;

import com.book.dto.AuthResponse;
import com.book.dto.LoginRequest;
import com.book.dto.RegisterRequest;
import com.book.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
    }

    @Test
    void register_ValidRequest_ReturnsOk() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(authService.register(any())).thenReturn(new AuthResponse("token", "testuser", "USER"));

        ResponseEntity<?> response = authController.register(registerRequest, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof AuthResponse);
    }

    @Test
    void register_InvalidRequest_ReturnsBadRequest() {
        when(bindingResult.hasErrors()).thenReturn(true);
        FieldError fieldError = new FieldError("registerRequest", "username", "Username is required");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<?> response = authController.register(registerRequest, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
    }

    @Test
    void login_ValidRequest_ReturnsOk() {
        when(authService.login(any(), any())).thenReturn(new AuthResponse("token", "testuser", "USER"));

        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token", response.getBody().getToken());
    }
}

