package com.desafio2.demo2.service;

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
        User user = new User();
        user.setId(1L);
        user.setName("Marcos");

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = service.findById(1L);

        assertThat(result.getName()).isEqualTo("Marcos");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldDeleteUserWhenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {
        when(repository.existsById(2L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(2L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
