package com.trackspense.services;

import com.trackspense.data.models.User;
import com.trackspense.data.repos.UserRepo;
import com.trackspense.dto.requests.RegisterUserRequest;
import com.trackspense.dto.responses.UserResponse;
import com.trackspense.exceptions.EmailAlreadyExistsException;
import com.trackspense.exceptions.InvalidEmailFormatException;
import com.trackspense.exceptions.InvalidPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@Import(UserServiceImpl.class)
public class UserServiceTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void cleanDB(){
        userRepo.deleteAll();
    }

    @Test
    void registerUser_shouldSaveUserAndReturnUserResponse(){
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Kelvin");
        request.setEmail("kel@gmail.com");
        request.setPassword("pass360");

        UserResponse response = userService.registerUser(request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getUserName()).isEqualTo("Kelvin");
        assertThat(response.getEmail()).isEqualTo("kel@gmail.com");

        User saved = userRepo.findById(response.getId()).orElseThrow(() -> new RuntimeException("User not found in DataBase"));
        assertThat(saved.getPassword()).isNotEqualTo("pass360");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertThat(encoder.matches("pass360", saved.getPassword())).isTrue();
    }

    @Test
    void registerUser_shouldFailWhenUsernameIsEmpty(){
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("   ");
        request.setEmail("glo@gmail.com");
        request.setPassword("glo360");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
    }

    @Test
    void registerUser_shouldFailWhenEmailIsInvalid(){
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Motun");
        request.setEmail("tuntun-email");
        request.setPassword("tuntun360");

        assertThrows(InvalidEmailFormatException.class, () -> userService.registerUser(request));
    }

    @Test
    void registerUser_shouldFailWhenEmailAlreadyExists() {
        RegisterUserRequest request1 = new RegisterUserRequest();
        request1.setUserName("Dare");
        request1.setEmail("dare@gmail.com");
        request1.setPassword("dare360");

        userService.registerUser(request1);

        RegisterUserRequest request2 = new RegisterUserRequest();
        request2.setUserName("Ibrahim");
        request2.setEmail("dare@gmail.com");
        request2.setPassword("ibro360");

        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(request2));
    }

    @Test
    void registerUser_shouldFailWhenPasswordTooShort() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Eskay");
        request.setEmail("sk@gmail.com");
        request.setPassword("sk1");

        assertThrows(InvalidPasswordException.class, () -> userService.registerUser(request));
    }
}
