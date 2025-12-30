package com.desafio2.demo2.controller;

import com.desafio2.demo2.exception.UserNotFoundException;
import com.desafio2.demo2.model.User;
import com.desafio2.demo2.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Marcos");
        user.setEmail("marcos@email.com");

        when(service.create(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Marcos"));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Marcos");

        when(service.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Marcos"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(service.findById(99L))
                .thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with id: 99"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(service).delete(1L);
    }
}
