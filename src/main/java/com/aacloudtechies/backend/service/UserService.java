package com.aacloudtechies.backend.service;

import com.aacloudtechies.backend.dto.UserDto;
import com.aacloudtechies.backend.dto.UserRequest;
import com.aacloudtechies.backend.entity.UserEntity;
import com.aacloudtechies.backend.exception.ResourceNotFoundException;
import com.aacloudtechies.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto register(UserRequest request) {
        logger.info("Registering new user username={} email={}", request.getUsername(), request.getEmail());
        UserEntity entity = new UserEntity();
        entity.setUsername(request.getUsername());
        entity.setPassword(request.getPassword());
        entity.setEmail(request.getEmail());
        entity.setFullName(request.getFullName());

        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new IllegalArgumentException("Username already exists");
        });
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new IllegalArgumentException("Email already exists");
        });

        UserEntity saved = userRepository.save(entity);
        logger.info("New user persisted with id={}", saved.getId());
        return toDto(saved);
    }

    public List<UserDto> getAllUsers() {
        logger.debug("Retrieving all users from repository");
        List<UserDto> users = userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        logger.info("Retrieved {} users", users.size());
        return users;
    }

    public UserDto getUserById(Long id) {
        logger.info("Looking up user by id={}", id);
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public UserDto updateUser(Long id, UserRequest request) {
        logger.info("Updating user id={}", id);
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        existing.setUsername(request.getUsername());
        existing.setPassword(request.getPassword());
        existing.setEmail(request.getEmail());
        existing.setFullName(request.getFullName());

        UserDto updated = toDto(userRepository.save(existing));
        logger.info("Updated user id={}", id);
        return updated;
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user id={}", id);
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(existing);
        logger.info("Deleted user id={}", id);
    }

    public void forgotPassword(String username, String newPassword) {
        logger.info("Processing forgot-password for username={}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Username not found"));
        user.setPassword(newPassword);
        userRepository.save(user);
        logger.info("Password updated for username={}", username);
    }

    private UserDto toDto(UserEntity entity) {
        return new UserDto(entity.getId(), entity.getUsername(), entity.getEmail(), entity.getFullName());
    }
}
