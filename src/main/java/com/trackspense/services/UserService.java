package com.trackspense.services;

import com.trackspense.dto.requests.RegisterUserRequest;
import com.trackspense.dto.responses.LoginResponse;
import com.trackspense.dto.responses.UserResponse;

public interface UserService {
    UserResponse registerUser(RegisterUserRequest request);
    LoginResponse login(String email, String password);
}
