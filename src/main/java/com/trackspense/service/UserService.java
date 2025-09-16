package com.trackspense.service;

import com.trackspense.data.model.User;
import com.trackspense.data.repo.UserRepo;
import com.trackspense.exception.InvalidEmailFormatException;
import com.trackspense.exception.InvalidPasswordException;
import com.trackspense.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public User registerUser(User user){
        if(!EMAIL_PATTERN.matcher(user.getEmail()).matches()){
            throw new InvalidEmailFormatException("Your Email no valid...check am!");
        }

        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser.isPresent()){
            throw new RuntimeException("Email is already registered...no vex");
        }
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
