package org.example.service;

import org.example.exception.ResourceNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        if (user.getId() == null) {
            if (userRepository.existsByLogin(user.getLogin())) {
                throw new IllegalArgumentException("Login already exists");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            if (user.getCreatedAt() == null) {
                user.setCreatedAt(LocalDateTime.now());
            }
        } else {
            User existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getId()));

            if (!user.getLogin().equals(existingUser.getLogin()) &&
                    userRepository.existsByLogin(user.getLogin())) {
                throw new IllegalArgumentException("Login already exists");
            }

            if (!user.getEmail().equals(existingUser.getEmail()) &&
                    userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        }

        return userRepository.save(user);
    }


    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("User not found with id: " + id));
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public boolean loginExists(String login) {
        return userRepository.existsByLogin(login);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}