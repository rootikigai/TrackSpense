package com.trackspense.service;

import com.trackspense.data.model.User;
import com.trackspense.data.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public User registerUser(User user){
        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser.isPresent()){
            throw new RuntimeException("Email is already registered...no vex");
        }
        return userRepo.save(user);
    }

    public User login(String email, String password){
        Optional<User> existingUser = userRepo.findByEmail(email);
        if (existingUser.isPresent()){
            if (existingUser.get().getPassword().equals(password)){
                return existingUser.get();
            }
            else{
                throw new RuntimeException("Invalid password!");
            }
        }
        else {
            throw new RuntimeException("User not found!");
        }
    }

    public User findUserByEmail(String email){
        return userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
