package com.trackspense.services;

import com.trackspense.data.models.User;
import com.trackspense.data.repos.UserRepo;
import com.trackspense.dto.requests.RegisterUserRequest;
import com.trackspense.dto.responses.UserResponse;
import com.trackspense.exceptions.EmailAlreadyExistsException;
import com.trackspense.exceptions.InvalidEmailFormatException;
import com.trackspense.exceptions.InvalidPasswordException;
import com.trackspense.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public UserResponse registerUser(RegisterUserRequest request){
        if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (request.getEmail() == null || !EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new InvalidEmailFormatException("Invalid email format");
        }

        Optional<User> existingUser = userRepo.findByEmail(request.getEmail());
        if (existingUser.isPresent()){
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new InvalidPasswordException("Password must be at least 6 characters long");
        }

        User user = new User();
        user.setUserName(request.getUserName().trim());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepo.save(user);
        return UserMapper.toResponse(savedUser);
    }

//    public LoginResponse login(String email, String password){
//        if(!EMAIL_PATTERN.matcher(email).matches()){
//            throw new InvalidEmailFormatException("Your Email no valid...check am!");
//        }
//
//        User existingUser = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Dem no fit find am!"));
//        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
//            throw new InvalidPasswordException("Your password no valid...check am!");
//        }
//        return new LoginResponse(UserMapper.toResponse(existingUser), "Login Successful");
//    }
//
//    public UserResponse findUserByEmail(String email) {
//        if (!EMAIL_PATTERN.matcher(email).matches()) {
//            throw new InvalidEmailFormatException("Your Email no valid...check am!");
//        }
//
//        User existingUser = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Dem no fit find am!"));
//        return UserMapper.toResponse(existingUser);
//    }
}
