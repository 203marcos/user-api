package com.desafio2.demo2.controller;

import com.desafio2.demo2.dto.UserRequestDTO;
import com.desafio2.demo2.dto.UserResponseDTO;
import com.desafio2.demo2.exception.UserNotFoundException;
import com.desafio2.demo2.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(ValidationAutoConfiguration.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUser() throws Exception {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("Marcos");
        request.setEmail("marcos@email.com");

        UserResponseDTO response = new UserResponseDTO(1L, "Marcos", "marcos@email.com");

        when(service.create(any(UserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Marcos")));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        Long userId = 1L;
        UserRequestDTO updateRequest = new UserRequestDTO("João Updated", "joao.updated@email.com");
        UserResponseDTO response = new UserResponseDTO(userId, "João Updated", "joao.updated@email.com");

        when(service.update(eq(userId), any(UserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("João Updated")))
                .andExpect(jsonPath("$.email", is("joao.updated@email.com")));

        verify(service).update(eq(userId), any(UserRequestDTO.class));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistent() throws Exception {
        Long userId = 99L;
        UserRequestDTO updateRequest = new UserRequestDTO("Test", "test@email.com");

        when(service.update(eq(99L), any(UserRequestDTO.class)))
                .thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", containsString("not found")));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        UserResponseDTO response = new UserResponseDTO(1L, "Marcos", "marcos@email.com");

        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Marcos")));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        UserResponseDTO user1 = new UserResponseDTO(1L, "Marcos", "marcos@email.com");
        UserResponseDTO user2 = new UserResponseDTO(2L, "Ana", "ana@email.com");

        when(service.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Marcos")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Ana")));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(service.findById(99L)).thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", containsString("not found")));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    void shouldReturn400WhenNameIsEmpty() throws Exception {
        UserRequestDTO invalid = new UserRequestDTO("", "valid@email.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("name")));
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        UserRequestDTO invalid = new UserRequestDTO("John", "not-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("email")));
    }

    @Test
    void shouldReturn400WhenEmailIsEmpty() throws Exception {
        UserRequestDTO invalid = new UserRequestDTO("John", "");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("email")));
    }
}
