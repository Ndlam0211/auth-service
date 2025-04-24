package com.example.ecommerce.modules.user.dtos.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRequest {

    @NotBlank(message = "mustn't be blank")
    private String token;

}
