package com.example.ecommerce.modules.user.dtos.requests.Permission;


import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateRequest {

    @NotBlank(message = "mustn't be blank")
    private String name;

    @NotNull(message = "mustn't be null")
    @Min(value = 0,message = "must be greater than or equal to 0")
    @Max(value=2,message="must be less than or equal to 2")
    private Integer publish;

    @NotNull(message = "mustn't be blank")
    private Long userId;
}
