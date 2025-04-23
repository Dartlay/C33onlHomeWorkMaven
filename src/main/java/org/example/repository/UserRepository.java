package org.example.repository;

import org.example.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    List<User> findAll();
    User save(User user);
    void update(User user);
    void delete(Long id);
    boolean existsByLogin(String login);
}