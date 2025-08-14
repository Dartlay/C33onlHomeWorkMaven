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

    long countByRole(User.Role role);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    // Для проверки уникальности при регистрации
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}