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
import com.desafio2.demo2.dto.UserRequestDTO;
import com.desafio2.demo2.dto.UserResponseDTO;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
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

    @Test
    void shouldCreateUserSuccessfully() {
        UserRequestDTO request = new UserRequestDTO("João", "joao@email.com");

        User userToSave = new User();
        userToSave.setName("João");
        userToSave.setEmail("joao@email.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("João");
        savedUser.setEmail("joao@email.com");

        when(repository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = service.create(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("João");
        assertThat(result.getEmail()).isEqualTo("joao@email.com");

        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        Long userId = 1L;
        UserRequestDTO request = new UserRequestDTO("João Atualizado", "joao.novo@email.com");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("João");
        existingUser.setEmail("joao@email.com");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("João Atualizado");
        updatedUser.setEmail("joao.novo@email.com");

        when(repository.findById(userId)).thenReturn(Optional.of(existingUser));

        when(repository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDTO result = service.update(userId, request);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("João Atualizado");
        assertThat(result.getEmail()).isEqualTo("joao.novo@email.com");

        verify(repository).findById(userId);
        verify(repository).save(any(User.class));
    }

    @Test
    void shouldReturnAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("João");
        user1.setEmail("joao@email.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Maria");
        user2.setEmail("maria@email.com");

        when(repository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponseDTO> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("João");
        assertThat(result.get(1).getName()).isEqualTo("Maria");

        verify(repository).findAll();
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistent() {
        Long userId = 99L;
        UserRequestDTO request = new UserRequestDTO("Test", "test@email.com");

        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(userId, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository).findById(userId);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setName("João");
        user.setEmail("joao@email.com");

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        doNothing().when(repository).delete(any(User.class));

        service.delete(userId);

        verify(repository).findById(userId);
        verify(repository).delete(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistent() {
        Long userId = 99L;

        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(repository, never()).delete(any());
    }

    
}
