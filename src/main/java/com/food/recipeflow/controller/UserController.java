package com.food.recipeflow.controller;

import com.food.recipeflow.dto.SignupRequest;
import com.food.recipeflow.dto.UserResponse;
import com.food.recipeflow.entity.User;
import com.food.recipeflow.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import com.food.recipeflow.dto.LoginRequest;
import com.food.recipeflow.security.JwtService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserController(UserService userService, AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody SignupRequest signupRequest) {

        if (userService.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email not available");
        }

        if (userService.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username not availabel");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        User savedUser = userService.saveUser(user);
        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        User user = userService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        String token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "user", new UserResponse(user.getId(), user.getUsername(), user.getEmail())));
    }
}
