package com.example.ecommerce.modules.user.mappers;

import com.example.ecommerce.annotations.BaseMapperAnnotation;
import com.example.ecommerce.mappers.BaseMapper;
import com.example.ecommerce.modules.user.dtos.UserCatalogueDTO;
import com.example.ecommerce.modules.user.dtos.requests.UserCatalogue.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.UserCatalogue.UpdateRequest;
import com.example.ecommerce.modules.user.entities.UserCatalogue;
import org.mapstruct.*;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface UserCatalogueMapper extends BaseMapper<UserCatalogue, UserCatalogueDTO, StoreRequest, UpdateRequest> {

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserCatalogue toEntity(StoreRequest createRequest);

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateRequest updateRequest,@MappingTarget UserCatalogue entity);
}
