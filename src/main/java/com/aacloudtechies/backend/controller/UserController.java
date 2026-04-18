package com.aacloudtechies.backend.controller;

import com.aacloudtechies.backend.dto.ForgotPasswordRequest;
import com.aacloudtechies.backend.dto.UserDto;
import com.aacloudtechies.backend.dto.UserRequest;
import com.aacloudtechies.backend.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserRequest request) {
        logger.info("Register user request received for username={}", request.getUsername());
        UserDto created = userService.register(request);
        logger.info("User registered successfully with id={}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserRequest request) {
        logger.info("Create user request received for username={}", request.getUsername());
        UserDto created = userService.register(request);
        logger.info("User created successfully with id={}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        logger.info("Fetching all users");
        List<UserDto> users = userService.getAllUsers();
        logger.info("Fetched {} users", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        logger.info("Fetching user by id={}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        logger.info("Update user request for id={}", id);
        return userService.updateUser(id, request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        logger.info("Forgot password request received for username={}", request.getUsername());
        userService.forgotPassword(request.getUsername(), request.getPassword());
        logger.info("Password update completed for username={}", request.getUsername());
        return ResponseEntity.ok("Password updated successfully if the username exists.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Delete user request for id={}", id);
        userService.deleteUser(id);
        logger.info("Deleted user with id={}", id);
        return ResponseEntity.noContent().build();
    }
}
