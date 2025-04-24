package com.example.ecommerce.modules.user.dtos.requests.User;

import com.example.ecommerce.annotations.UniqeEmail;
import com.example.ecommerce.annotations.UniqePhone;
import com.example.ecommerce.annotations.UniqeUsername;
import com.example.ecommerce.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class StoreRequest {

    @NotBlank(message = "mustn't be blank")
    @UniqeUsername
    private String userName;

    @NotBlank(message = "mustn't be blank")
    @Size(min = 8,message= "must be greater than or equal to 8 characters")
    private String password;

    @NotBlank(message = "mustn't be blank")
    @UniqeEmail
    @ValidEmail
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10}$", message = "Phone number must be 10 digits") // phone number must be 10 digits
    @UniqePhone
    private String phone;
    private String address;
    private String image;
    private Integer age;

    @NotEmpty(message = "User not granted UserCatalogue yet")
    private List<Long> userCatalogues;
}
