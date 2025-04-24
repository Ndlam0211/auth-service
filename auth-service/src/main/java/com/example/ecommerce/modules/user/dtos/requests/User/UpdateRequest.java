package com.example.ecommerce.modules.user.dtos.requests.User;


import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class UpdateRequest {
    @NotBlank(message = "mustn't be blank")
    private String userName;

    @NotBlank(message = "mustn't be blank")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10}$", message = "Phone number must be 10 digits") // phone number must be 10 digits
    private String phone;
    private String address;
    private String image;
    private Integer age;

    @NotEmpty(message = "User not granted UserCatalogue yet")
    private List<Long> userCatalogues;
}
