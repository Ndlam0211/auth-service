package com.example.ecommerce.modules.user.mappers;

import com.example.ecommerce.annotations.BaseMapperAnnotation;
import com.example.ecommerce.mappers.BaseMapper;
import com.example.ecommerce.modules.user.dtos.UserDTO;
import com.example.ecommerce.modules.user.dtos.requests.User.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.User.UpdateRequest;
import com.example.ecommerce.modules.user.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDTO, StoreRequest, UpdateRequest> {

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "userCatalogues", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toEntity(StoreRequest createRequest);

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "userCatalogues", ignore = true)
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateRequest updateRequest,@MappingTarget User entity);
}
