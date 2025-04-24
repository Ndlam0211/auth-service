package com.example.ecommerce.modules.user.services;

import com.example.ecommerce.modules.user.dtos.PermissionDTO;
import com.example.ecommerce.modules.user.dtos.requests.Permission.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.Permission.UpdateRequest;
import com.example.ecommerce.services.BaseServiceInterface;

public interface PermissionService extends BaseServiceInterface<PermissionDTO, StoreRequest, UpdateRequest, Long> {}
