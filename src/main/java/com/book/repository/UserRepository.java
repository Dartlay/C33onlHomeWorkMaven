package com.book.repository;

import com.book.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Для аутентификации по username
    Optional<User> findByUsername(String username);

    // Для аутентификации по email
    Optional<User> findByEmail(String email);

    // Для проверки уникальности при регистрации
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    // Дополнительные методы для админ-панели
    List<User> findAllByRole(User.Role role);

    List<User> findAllByEnabled(boolean enabled);
}