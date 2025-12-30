package com.desafio2.demo2.controller;

import com.desafio2.demo2.dto.UserRequestDTO;
import com.desafio2.demo2.dto.UserResponseDTO;
import com.desafio2.demo2.exception.UserNotFoundException;
import com.desafio2.demo2.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
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
        Long userId = Long.valueOf(1);
        UserResponseDTO response = new UserResponseDTO(userId, "Marcos", "marcos@email.com");

        when(service.create(any(UserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Marcos")));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        Long userId = (Long) 1L;
        UserResponseDTO response = new UserResponseDTO(userId, "Marcos", "marcos@email.com");

        when(service.findById(userId)).thenReturn(response);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Marcos")));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        Long userId = Long.valueOf(1);
        Long userId2 = Long.valueOf(2);
        UserResponseDTO user1 = new UserResponseDTO(userId, "Marcos", "marcos@email.com");
        UserResponseDTO user2 = new UserResponseDTO(userId2, "Ana", "ana@email.com");

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
        Long userId = Long.valueOf(99);

        when(service.findById(userId)).thenThrow(new UserNotFoundException(userId));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with id: 99"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        Long userId = Long.valueOf(1);

        doNothing().when(service).delete(userId);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(service).delete(userId);
    }
}
