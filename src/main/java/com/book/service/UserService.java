package com.book.service;

import com.book.dto.UserDTO;
import com.book.model.User;
import com.book.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::fromUser)
                .collect(Collectors.toList());
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserDTO.fromUser(user);
    }

    @Transactional
    public void updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // проверка уникальности
        if (userRepository.existsByUsernameAndIdNot(userDTO.getUsername(), id)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmailAndIdNot(userDTO.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(User.Role.valueOf(userDTO.getRole()));
        user.setEnabled(userDTO.isEnabled());

        userRepository.save(user);
    }


    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // дополнительные проверки
        if (user.getRole() == User.Role.ADMIN) {
            long adminCount = userRepository.countByRole(User.Role.ADMIN);
            if (adminCount <= 1) {
                throw new IllegalStateException("Cannot delete the last admin");
            }
        }

        userRepository.delete(user);
    }
}