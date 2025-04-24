package com.example.ecommerce.modules.user.dtos.responses;

import com.example.ecommerce.modules.user.dtos.UserDTO;

public class LoginResponse {

    private final String token;
    private final String refreshToken;
    private final UserDTO userResponse;

    public LoginResponse(String token, String refreshToken, UserDTO userResponse){
        this.token = token;
        this.refreshToken = refreshToken;
        this.userResponse = userResponse;
    }

    public String getToken(){
        return token;
    }

    public UserDTO getUserResponse(){
        return userResponse;
    }

    public String getRefreshToken(){
        return refreshToken;
    }
}
