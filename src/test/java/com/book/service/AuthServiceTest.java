package com.book.service;

import com.book.dto.AuthResponse;
import com.book.exception.PasswordCompromisedException;
import com.book.exception.WeakPasswordException;
import com.book.model.User;
import com.book.repository.UserRepository;
import com.book.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("StrongPass1!");
    }

    @Test
    void register_ValidUser_ReturnsAuthResponse() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(testUser);
        when(tokenProvider.generateToken(any())).thenReturn("testToken");

        AuthResponse response = authService.register(testUser);

        assertNotNull(response);
        assertEquals("testToken", response.getToken());
        verify(userRepository).save(testUser);
    }

    @Test
    void register_ExistingUsername_ThrowsException() {
        when(userRepository.existsByUsername(any())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(testUser));
    }

    @Test
    void login_ValidCredentials_ReturnsAuthResponse() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByUsername(any())).thenReturn(java.util.Optional.of(testUser));
        when(tokenProvider.generateToken(any())).thenReturn("testToken");

        AuthResponse response = authService.login("testuser", "password");

        assertNotNull(response);
        assertEquals("testToken", response.getToken());
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () ->
                authService.login("testuser", "wrongpass"));
    }


}

