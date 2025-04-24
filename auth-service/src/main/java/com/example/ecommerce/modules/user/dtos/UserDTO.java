package com.example.ecommerce.modules.user.dtos;

import com.example.ecommerce.modules.user.entities.UserCatalogue;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private final Long id;
    private final String email;
    private final String userName;
    private final String phone;
    private final Set<UserCatalogue> userCatalogues;
}
