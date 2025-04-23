package org.example.service.impl;

import org.example.exception.UserNotFoundException;
import org.example.exception.ValidationException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        return userRepository.save(user);
    }

    @Override
    public void updateUserLogin(Long id, String newLogin) {
        if (newLogin == null || newLogin.trim().isEmpty()) {
            throw new ValidationException("Login cannot be empty");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (userRepository.existsByLogin(newLogin)) {
            throw new ValidationException("Login already exists: " + newLogin);
        }

        user.setLogin(newLogin);
        userRepository.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.delete(id);
    }

    private void validateUser(User user) {
        if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
            throw new ValidationException("Login cannot be empty");
        }
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new ValidationException("Login already exists: " + user.getLogin());
        }
    }
}