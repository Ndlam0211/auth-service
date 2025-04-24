package com.example.ecommerce.modules.user.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Email(message = "must be a valid email")
    @NotBlank(message = "mustn't be blank")
    private String email;

    @Size(min = 6,message = "must be at least 6 characters")
    private String password;
}
