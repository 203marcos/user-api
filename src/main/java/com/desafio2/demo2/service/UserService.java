package com.desafio2.demo2.service;

import com.desafio2.demo2.dto.UserRequestDTO;
import com.desafio2.demo2.dto.UserResponseDTO;
import com.desafio2.demo2.exception.UserNotFoundException;
import com.desafio2.demo2.model.User;
import com.desafio2.demo2.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponseDTO create(UserRequestDTO request) {
        logger.info("Creating user with email: {}", request.getEmail());

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User saved = repository.save(user);
        logger.debug("User created successfully with id: {} and name: {}", saved.getId(), saved.getName());

        return new UserResponseDTO(saved.getId(), saved.getName(), saved.getEmail());
    }

    public UserResponseDTO findById(Long id) {
        logger.info("Finding user by id: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new UserNotFoundException(id);
                });

        logger.debug("User found: {}", user.getName());
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    public List<UserResponseDTO> findAll() {
        logger.info("Fetching all users");

        List<UserResponseDTO> users = repository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());

        logger.debug("Found {} users", users.size());
        return users;
    }

    public UserResponseDTO update(Long id, UserRequestDTO request) {
        logger.info("Updating user with id: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cannot update - user not found with id: {}", id);
                    return new UserNotFoundException(id);
                });

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User updated = repository.save(user);

        logger.debug("User updated with id: {}", id);

        return new UserResponseDTO(updated.getId(), updated.getName(), updated.getEmail());
    }

    public void delete(Long id) {
        logger.info("Deleting user with id: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cannot delete - user not found with id: {}", id);
                    return new UserNotFoundException(id);
                });

        repository.delete(user);
        logger.info("User deleted successfully with id: {}", id);
    }


}