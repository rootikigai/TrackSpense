package com.trackspense.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {
    @NotBlank(message = "Username is a must!")
    private String userName;
    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is a must!")
    private String email;
    @NotBlank(message = "Password is a must!")
    private String password;
}
