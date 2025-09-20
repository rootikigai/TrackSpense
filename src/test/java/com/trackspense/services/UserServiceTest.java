package com.trackspense.services;

import com.trackspense.data.models.User;
import com.trackspense.data.repos.UserRepo;
import com.trackspense.dto.requests.RegisterUserRequest;
import com.trackspense.dto.responses.LoginResponse;
import com.trackspense.dto.responses.UserResponse;
import com.trackspense.exceptions.EmailAlreadyExistsException;
import com.trackspense.exceptions.InvalidEmailFormatException;
import com.trackspense.exceptions.InvalidPasswordException;
import com.trackspense.exceptions.UserNotFoundException;
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

    @Test
    void registerUser_shouldFailWhenRequestIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null));
    }

    @Test
    void testThatLoginShouldSucceedWithValidDetails(){
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Kelvin");
        request.setEmail("kel360@gmail.com");
        request.setPassword("pass123");
        userService.registerUser(request);

        LoginResponse response = userService.login("kel360@gmail.com", "pass123");

        assertThat(response).isNotNull();
        assertThat(response.getUser().getEmail()).isEqualTo("kel360@gmail.com");
        assertThat(response.getUser().getUserName()).isEqualTo("Kelvin");
        assertThat(response.getMessage()).isEqualTo("Login Successful");
    }

    @Test
    void testThatLoginShouldFailWithInvalidPassword(){
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Ikigai");
        request.setEmail("ikigai@gmail.com");
        request.setPassword("ikigai360");
        userService.registerUser(request);

        assertThrows(InvalidPasswordException.class, () -> userService.login("ikigai@gmail.com", "wrong360"));
    }

    @Test
    void testThatLoginShouldFailWhenUserNotFound(){
        assertThrows(UserNotFoundException.class, () -> userService.login("notfound@gmail.com", "pass360"));
    }

    @Test
    void testThatLoginShouldFailWhenEmailIsInvalid(){
        assertThrows(InvalidEmailFormatException.class, () -> userService.login("wrong.gmail.com", "pass360"));
    }

    @Test
    void login_shouldFailWhenEmailIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.login(null, "pass360"));
    }

    @Test
    void login_shouldFailWhenPasswordIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.login("test@gmail.com", null));
    }

    @Test
    void testThatLoginResponseUserIsConsistentWithDatabase() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName("Kelvin");
        request.setEmail("kelvin@gmail.com");
        request.setPassword("pass360");
        userService.registerUser(request);

        LoginResponse response = userService.login("kelvin@gmail.com", "pass360");

        User saved = userRepo.findByEmail("kelvin@gmail.com")
                .orElseThrow(() -> new RuntimeException("User not found in DataBase"));

        assertThat(response.getUser().getId()).isEqualTo(saved.getId());
        assertThat(response.getUser().getUserName()).isEqualTo(saved.getUserName());
        assertThat(response.getUser().getEmail()).isEqualTo(saved.getEmail());
    }

    @Test
    void registerUser_shouldFailWhenUsernameIsNullOrEmpty() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUserName(null);
        request.setEmail("valid@gmail.com");
        request.setPassword("valid360");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));

        request.setUserName("   ");
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
    }

    @Test
    void registerUser_shouldFailForMoreInvalidEmailFormats() {
        String[] invalidEmails = {"a@b", "@@@", "   ", "user@.com", "user@domain"};
        for (String badEmail : invalidEmails) {
            RegisterUserRequest request = new RegisterUserRequest();
            request.setUserName("Tester");
            request.setEmail(badEmail);
            request.setPassword("valid360");
            assertThrows(InvalidEmailFormatException.class, () -> userService.registerUser(request));
        }
    }
}
