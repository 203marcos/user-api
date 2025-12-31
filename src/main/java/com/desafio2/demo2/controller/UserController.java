package com.desafio2.demo2.controller;

import com.desafio2.demo2.dto.UserRequestDTO;
import com.desafio2.demo2.dto.UserResponseDTO;
import com.desafio2.demo2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User created")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO dto) {
        logger.info("POST /users - Creating user with email: {}", dto.getEmail());
        UserResponseDTO created = service.create(dto);
        logger.info("User created with id: {}", created.getId());
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Update user by ID")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid UserRequestDTO dto) {

        logger.info("PUT /users/{} - Updating user", id);

        UserResponseDTO updated = service.update(id, dto);

        logger.info("User {} updated successfully", id);

        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        logger.info("GET /users/{} - Finding user", id);
        UserResponseDTO user = service.findById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        logger.info("GET /users - Fetching all users");
        List<UserResponseDTO> users = service.findAll();
        logger.info("Returned {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("DELETE /users/{} - Deleting user", id);
        service.delete(id);
        logger.info("User {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}