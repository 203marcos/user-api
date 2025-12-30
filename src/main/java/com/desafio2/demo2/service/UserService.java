package com.desafio2.demo2.service;

import com.desafio2.demo2.dto.UserRequestDTO;
import com.desafio2.demo2.dto.UserResponseDTO;
import com.desafio2.demo2.exception.UserNotFoundException;
import com.desafio2.demo2.model.User;
import com.desafio2.demo2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponseDTO create(UserRequestDTO request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User saved = repository.save(user);

        return new UserResponseDTO(saved.getId(), saved.getName(), saved.getEmail());
    }

    public UserResponseDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        repository.delete(user);
    }

    public List<UserResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
    }
}
