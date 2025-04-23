package org.example.service;
import org.example.model.User;


import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    User createUser(User user);
    void updateUserLogin(Long id, String newLogin);
    void deleteUser(Long id);
}