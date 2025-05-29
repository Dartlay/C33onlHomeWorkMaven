package org.example.repository;


import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT id, login, name, email, created_at FROM users LIMIT 1",
            nativeQuery = true)
    Map<String, Object> testColumns();

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);
}
