package com.trackspense.services;

import com.trackspense.data.models.User;
import com.trackspense.data.repos.UserRepo;
import com.trackspense.dto.RegisterUserRequest;
import com.trackspense.exceptions.EmailAlreadyExistsException;
import com.trackspense.exceptions.InvalidEmailFormatException;
import com.trackspense.exceptions.InvalidPasswordException;
import com.trackspense.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public User registerUser(RegisterUserRequest request){
        if(!EMAIL_PATTERN.matcher(request.getEmail()).matches()){
            throw new InvalidEmailFormatException("Your Email no valid...check am!");
        }

        Optional<User> existingUser = userRepo.findByEmail(request.getEmail());
        if (existingUser.isPresent()){
            throw new EmailAlreadyExistsException("Email is already registered...no vex");
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepo.save(user);
    }

    public User login(String email, String password){
        if(!EMAIL_PATTERN.matcher(email).matches()){
            throw new InvalidEmailFormatException("Your Email no valid...check am!");
        }

        User existingUser = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Dem no fit find am!"));
        if (!existingUser.getPassword().equals(password)) {
            throw new InvalidPasswordException("Your password no valid...check am!");
        }
        return existingUser;
    }

    public User findUserByEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailFormatException("Your Email no valid...check am!");
        }
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Dem no fit find am!"));
    }
}
