package com.book.service;

import com.book.dto.AuthResponse;
import com.book.exception.PasswordCompromisedException;
import com.book.exception.WeakPasswordException;
import com.book.model.User;
import com.book.repository.UserRepository;
import com.book.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final List<String> COMMON_PASSWORDS = Arrays.asList(
            "123456", "password", "qwerty", "111111", "admin"
    );

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RestTemplate restTemplate;

    public AuthResponse register(User user) {
        validateUserCredentials(user);
        checkPasswordStrength(user.getPassword());
        checkPasswordCompromised(user.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        logger.info("User registered successfully: {}", user.getUsername());

        return generateAuthResponse(savedUser);
    }

    public AuthResponse login(String username, String password) {
        try {
            // 1. Аутентифицируем пользователя (проверка логина/пароля)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // 2. Получаем пользователя из БД, чтобы узнать его роль
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 3. Создаём новый Authentication с ролями
            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(user.getRole().getAuthority()) // "ROLE_ADMIN"
            );

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), // principal
                    null,              // credentials (не нужно, т.к. аутентификация уже прошла)
                    authorities        // роли пользователя
            );

            // 4. Сохраняем в SecurityContext
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            logger.info("User logged in successfully: {}", username);
            return generateAuthResponse(user);
        } catch (BadCredentialsException e) {
            logger.warn("Failed login attempt for username: {}", username);
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private void validateUserCredentials(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }
    }

    private void checkPasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new WeakPasswordException("Password must be at least 8 characters long");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new WeakPasswordException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new WeakPasswordException("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new WeakPasswordException("Password must contain at least one digit");
        }

        if (!password.matches(".*[@$!%*?&].*")) {
            throw new WeakPasswordException("Password must contain at least one special character (@$!%*?&)");
        }

        if (COMMON_PASSWORDS.contains(password.toLowerCase())) {
            throw new WeakPasswordException("Password is too common and insecure");
        }
    }

    private void checkPasswordCompromised(String password) {
        try {
            String sha1 = DigestUtils.sha1Hex(password).toUpperCase();
            String prefix = sha1.substring(0, 5);
            String suffix = sha1.substring(5);

            ResponseEntity<String> response = restTemplate.getForEntity(
                    "https://api.pwnedpasswords.com/range/" + prefix, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody().contains(suffix)) {
                throw new PasswordCompromisedException("This password has been compromised in data breaches");
            }
        } catch (Exception e) {
            logger.error("Error checking password with HIBP API: {}", e.getMessage());
            // Можно продолжить регистрацию, если сервис недоступен
        }
    }

    private AuthResponse generateAuthResponse(User user) {
        String token = tokenProvider.generateToken(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        null,
                        Collections.singletonList(
                                new SimpleGrantedAuthority(user.getRole().getAuthority())
                        )
                )
        );

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole().getAuthority()
        );
    }
}