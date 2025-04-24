package com.example.ecommerce.modules.user.services.impl;

import com.example.ecommerce.dtos.APIResponse;
import com.example.ecommerce.exceptions.ResourceExistedException;
import com.example.ecommerce.mappers.BaseMapper;
import com.example.ecommerce.modules.user.dtos.UserDTO;
import com.example.ecommerce.modules.user.dtos.requests.TokenRequest;
import com.example.ecommerce.modules.user.dtos.requests.LoginRequest;
import com.example.ecommerce.modules.user.dtos.requests.User.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.User.UpdateRequest;
import com.example.ecommerce.modules.user.dtos.responses.LoginResponse;
import com.example.ecommerce.modules.user.entities.User;
import com.example.ecommerce.modules.user.mappers.UserMapper;
import com.example.ecommerce.modules.user.repositories.UserRepo;
import com.example.ecommerce.modules.user.services.BlacklistTokenService;
import com.example.ecommerce.modules.user.services.UserService;
import com.example.ecommerce.security.JwtUtil;
import com.example.ecommerce.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl extends BaseService<User, UserDTO, StoreRequest, UpdateRequest, Long, UserRepo> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BlacklistTokenService blacklistTokenService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public APIResponse<LoginResponse> authenticate(LoginRequest loginRequest) {
        // khong tra ve chinh xac mat khau hay email khong dung de tranh hacker biet
        // check email
        User user = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // check password
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Invalid email or password");
        }

        // mapping userEntity to userDTO
        UserDTO userResponse = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .build();

        // genarate token
        String token = jwtUtil.generateToken(user.getId(),user.getEmail());
        // genarate refreshToken
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(),user.getEmail());

        LoginResponse loginResponse =  new LoginResponse(token, refreshToken,userResponse);

        return APIResponse.ok(loginResponse,"Login successfully");
    }

    @Override
    public void logout(String bearerToken) {
        String token = bearerToken.substring(7);

        TokenRequest blacklistTokenRequest = new TokenRequest();
        blacklistTokenRequest.setToken(token);

        blacklistTokenService.create(blacklistTokenRequest);
    }

    @Override
    protected String[] getSearchFields() {
        return new String[]{"userName"};
    }

    @Override
    protected UserRepo getRepository() {
        return this.userRepo;
    }

    @Override
    protected String[] getRelations() {
        return new String[]{"userCatalogues"};
    }

    @Override
    protected BaseMapper<User, UserDTO, StoreRequest, UpdateRequest> getMapper() {
        return this.userMapper;
    }

    @Override
    protected String getEntityName() {
        return "User";
    }

    @Override
    protected void preProcessCreate(StoreRequest request){
        if (request.getPassword() != null)
            request.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    @Override
    protected void preProcessUpdate(UpdateRequest request, Long id){
        Optional<User> emailCheckUser = userRepo.findByEmail(request.getEmail());
        Optional<User> userNameCheckUser = userRepo.findByUserName(request.getUserName());
        Optional<User> phoneCheckUser = userRepo.findByPhone(request.getPhone());
        if (emailCheckUser.isPresent()){
            if (!emailCheckUser.get().getId().equals(id)){
                throw new ResourceExistedException("Users","email",request.getEmail());
            }
        }
        if (userNameCheckUser.isPresent()){
            if (!userNameCheckUser.get().getId().equals(id)){
                throw new ResourceExistedException("Users","Username",request.getUserName());
            }
        }
        if (phoneCheckUser.isPresent()){
            if (!phoneCheckUser.get().getId().equals(id)){
                throw new ResourceExistedException("Users","phone number",request.getPhone());
            }
        }
    }
}
