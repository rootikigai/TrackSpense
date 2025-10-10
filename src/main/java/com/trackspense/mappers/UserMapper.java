package com.trackspense.mappers;

import com.trackspense.data.models.User;
import com.trackspense.dto.responses.UserResponse;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail());
    }
}
