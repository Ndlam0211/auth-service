package com.example.ecommerce.modules.user.controllers;


import com.example.ecommerce.controllers.BaseController;
import com.example.ecommerce.dtos.APIResponse;
import com.example.ecommerce.enums.PermissionEnum;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.modules.user.dtos.UserDTO;
import com.example.ecommerce.modules.user.dtos.requests.User.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.User.UpdateRequest;
import com.example.ecommerce.modules.user.entities.User;
import com.example.ecommerce.modules.user.repositories.UserRepo;
import com.example.ecommerce.modules.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Tag(name="User API")
@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController<UserDTO, StoreRequest, UpdateRequest, Long> {

    @Autowired
    private UserRepo userRepo;

    public UserController(UserService userService){
        super(userService, PermissionEnum.USER);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName() ;

        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User","email",email));

        UserDTO userResponse = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .phone((user.getPhone()))
                .userCatalogues(user.getUserCatalogues())
                .build();

        APIResponse<UserDTO> apiResponse = APIResponse.ok(userResponse,"User retrieved successfully");

        return ResponseEntity.ok(apiResponse);
    }

    @Override
    protected String getEntityName() {
        return "User";
    }
}
