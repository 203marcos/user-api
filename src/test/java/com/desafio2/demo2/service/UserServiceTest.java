package com.desafio2.demo2.service;

import com.desafio2.demo2.dto.UserResponseDTO;
import com.desafio2.demo2.exception.UserNotFoundException;
import com.desafio2.demo2.model.User;
import com.desafio2.demo2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    void shouldReturnUserWhenFoundById() {
        Long userId = Long.valueOf(1);
        User user = new User();
        user.setId(userId);
        user.setName("Marcos");
        user.setEmail("marcos@email.com");

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDTO result = service.findById(userId);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("Marcos");
        assertThat(result.getEmail()).isEqualTo("marcos@email.com");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        Long userId = Long.valueOf(99);

        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldDeleteUserWhenExists() {
        Long userId = 1L;
        User found = new User("Marcos", "marcos@email.com");
        found.setId(userId);

        when(repository.findById(userId)).thenReturn(Optional.of(found));

        service.delete(userId);

        verify(repository).delete(found);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {
        Long userId = 2L;

        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(userId))
                .isInstanceOf(UserNotFoundException.class);
    }

    
}
