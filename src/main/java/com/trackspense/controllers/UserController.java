package com.trackspense.controllers;

import com.trackspense.dto.requests.LoginRequest;
import com.trackspense.dto.requests.RegisterUserRequest;
import com.trackspense.dto.responses.LoginResponse;
import com.trackspense.dto.responses.UserResponse;
import com.trackspense.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterUserRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<java.util.Map<String, String>> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request.getEmail(), request.getPassword());
        java.util.Map<String, String> payload = java.util.Map.of(
                "token", response.getToken(),
                "email", response.getUser().getEmail()
        );
        return ResponseEntity.ok(payload);
    }
}
