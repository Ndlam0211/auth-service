package com.example.ecommerce.modules.user.dtos.requests.UserCatalogue;


import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class UpdateRequest {

    @NotBlank(message = "mustn't be blank")
    private String name;

    @NotNull(message = "mustn't be null")
    @Min(value = 0,message = "must be greater than or equal to 0")
    @Max(value=2,message="must be less than or equal to 2")
    private Integer publish;

    @NotEmpty(message = "UserCatalogue not granted permission yet")
    private List<Long> permissions;
}
