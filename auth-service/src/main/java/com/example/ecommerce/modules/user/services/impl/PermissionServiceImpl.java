package com.example.ecommerce.modules.user.services.impl;

import com.example.ecommerce.modules.user.dtos.PermissionDTO;
import com.example.ecommerce.modules.user.dtos.requests.Permission.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.Permission.UpdateRequest;
import com.example.ecommerce.modules.user.entities.Permission;
import com.example.ecommerce.modules.user.mappers.PermissionMapper;
import com.example.ecommerce.modules.user.repositories.PermissionRepo;
import com.example.ecommerce.modules.user.services.PermissionService;
import com.example.ecommerce.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends BaseService<Permission, PermissionDTO, StoreRequest, UpdateRequest,Long, PermissionRepo> implements PermissionService {
    @Autowired
    private PermissionRepo permissionRepo;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    protected String[] getSearchFields() {
        return new String[]{"name"};
    }

    @Override
    protected String getEntityName() {
        return "Permission";
    }

    @Override
    protected PermissionRepo getRepository() {
        return this.permissionRepo;
    }

    @Override
    protected PermissionMapper getMapper() {
        return this.permissionMapper;
    }
}
