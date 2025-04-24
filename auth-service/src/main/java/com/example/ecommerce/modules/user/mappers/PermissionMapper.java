package com.example.ecommerce.modules.user.mappers;

import com.example.ecommerce.annotations.BaseMapperAnnotation;
import com.example.ecommerce.mappers.BaseMapper;
import com.example.ecommerce.modules.user.dtos.PermissionDTO;
import com.example.ecommerce.modules.user.dtos.requests.Permission.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.Permission.UpdateRequest;
import com.example.ecommerce.modules.user.entities.Permission;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends BaseMapper<Permission, PermissionDTO, StoreRequest, UpdateRequest> {

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "userCatalogues", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Permission toEntity(StoreRequest createRequest);

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "userCatalogues", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateRequest updateRequest, @MappingTarget Permission entity);
}
