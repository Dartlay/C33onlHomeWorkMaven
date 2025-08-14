package com.book.service;

import com.book.dto.UserDTO;
import com.book.model.User;
import com.book.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
    }

    @Test
    void getAllUsers_ReturnsUserList() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        List<UserDTO> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
    }

    @Test
    void getUserById_ExistingId_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserDTO user = userService.getUserById(1L);

        assertEquals("testuser", user.getUsername());
    }

    @Test
    void getUserById_NonExistingId_ThrowsException() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void updateUser_ValidData_UpdatesUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updated");
        userDTO.setEmail("updated@example.com");
        userDTO.setRole("USER");
        userDTO.setEnabled(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsernameAndIdNot(any(), any())).thenReturn(false);
        when(userRepository.existsByEmailAndIdNot(any(), any())).thenReturn(false);

        userService.updateUser(1L, userDTO);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser_ExistingUser_DeletesUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        userService.deleteUser(1L);

        verify(userRepository).delete(testUser);
    }
}
