package com.example.ecommerce.modules.user.services;

import com.example.ecommerce.dtos.APIResponse;
import com.example.ecommerce.modules.user.dtos.UserDTO;
import com.example.ecommerce.modules.user.dtos.requests.LoginRequest;
import com.example.ecommerce.modules.user.dtos.requests.User.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.User.UpdateRequest;
import com.example.ecommerce.modules.user.dtos.responses.LoginResponse;
import com.example.ecommerce.services.BaseServiceInterface;
import org.hibernate.sql.Update;

public interface UserService extends BaseServiceInterface<UserDTO, StoreRequest, UpdateRequest, Long> {

    APIResponse<LoginResponse> authenticate(LoginRequest loginRequest);
    void logout(String bearerToken);
}
