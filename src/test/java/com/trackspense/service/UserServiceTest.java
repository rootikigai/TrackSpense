package com.trackspense.service;

import com.trackspense.data.model.User;
import com.trackspense.data.repository.UserRepo;
import com.trackspense.dto.requests.RegisterUserRequest;
import com.trackspense.dto.responses.LoginResponse;
import com.trackspense.dto.responses.UserResponse;
import com.trackspense.exceptions.EmailAlreadyExistsException;
import com.trackspense.exceptions.InvalidEmailFormatException;
import com.trackspense.exceptions.InvalidPasswordException;
import com.trackspense.exceptions.UserNotFoundException;
import com.trackspense.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // clear DB before each test so they don’t clash
        userRepo.deleteAll();
    }

    @Test
    void testRegisterUserSuccessfully() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Kelvin");
        request.setEmail("kelvin.unique@example.com");
        request.setPassword("password123");

        UserResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("Kelvin", response.getUserName());
        assertEquals("kelvin.unique@example.com", response.getEmail());
    }

    @Test
    void testRegisterUserFailsWhenEmailAlreadyExists() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Kelvin");
        request.setEmail("kelvin.unique@example.com");
        request.setPassword("password123");

        userService.registerUser(request);

        RegisterUserRequest secondRequest = new RegisterUserRequest();
        secondRequest.setUserName("Another Kelvin");
        secondRequest.setEmail("kelvin.unique@example.com");
        secondRequest.setPassword("anotherpass123");

        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(secondRequest));
    }

    @Test
    void testRegisterUserFailsWithInvalidEmail() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Kelvin");
        request.setEmail("invalidemail"); // no @ or domain
        request.setPassword("mypassword123");

        assertThrows(InvalidEmailFormatException.class, () -> userService.registerUser(request));
    }

    @Test
    void testRegisterUserFailsWithWeakPassword() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Kelvin");
        request.setEmail("kelvin.unique@example.com");
        request.setPassword("123"); // too short

        assertThrows(InvalidPasswordException.class, () -> userService.registerUser(request));
    }

    @Test
    void testPasswordIsEncodedOnRegistration() {
        String rawPassword = "mypassword123";

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Kelvin");
        request.setEmail("kelvin.unique@example.com");
        request.setPassword(rawPassword);

        UserResponse response = userService.registerUser(request);

        User savedUser = userRepo.findByEmail(response.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found in repo"));

        // I enforce that password in DB must not be plain
        assertNotEquals(rawPassword, savedUser.getPassword(),
                "Password should not be stored as plain text");

        // but that password must match when encoded
        assertTrue(passwordEncoder.matches(rawPassword, savedUser.getPassword()),
                "Encoded password should match raw password");
    }

    @Test
    void testLoginSuccessfully() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Kelvin");
        request.setEmail("kelvin.unique@example.com");
        request.setPassword("mypassword123");

        userService.registerUser(request);

        LoginResponse loginResponse = userService.login("kelvin.unique@example.com", "mypassword123");

        assertNotNull(loginResponse);
        assertEquals("Login Successful", loginResponse.getMessage());
        assertNotNull(loginResponse.getToken());
        assertTrue(jwtUtil.validateToken(loginResponse.getToken()));
    }

    @Test
    void testLoginFailsWithWrongPassword() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Kelvin");
        request.setEmail("kelvin.unique@example.com");
        request.setPassword("password123");

        userService.registerUser(request);

        assertThrows(InvalidPasswordException.class,
                () -> userService.login("kelvin.unique@example.com", "wrongpassword"));
    }

    @Test
    void testLoginFailsWithNonExistentEmail() {
        assertThrows(UserNotFoundException.class,
                () -> userService.login("notfound@example.com", "password123"));
    }
}
