package com.food.recipeflow.controller;

import com.food.recipeflow.dto.*;
import com.food.recipeflow.entity.User;
import com.food.recipeflow.service.OtpService;
import com.food.recipeflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import com.food.recipeflow.security.JwtService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;

    public UserController(UserService userService, AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, JwtService jwtService, OtpService otpService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public UserResponse registerUser(@Valid @RequestBody SignupRequest signupRequest) {

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

        otpService.sendOtp(savedUser.getEmail());

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getVerified());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        User user = userService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getVerified()) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        String token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "user",
                        new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getVerified())));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        otpService.sendOtp(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        otpService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
    }
}
