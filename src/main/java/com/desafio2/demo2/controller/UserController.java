package com.desafio2.demo2.controller;

import com.desafio2.demo2.dto.UserRequestDTO;
import com.desafio2.demo2.dto.UserResponseDTO;
import com.desafio2.demo2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO dto) {
        UserResponseDTO created = service.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public List<UserResponseDTO> list() {
        return service.findAll();
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
