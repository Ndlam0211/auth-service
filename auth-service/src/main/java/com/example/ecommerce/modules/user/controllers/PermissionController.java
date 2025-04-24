package com.example.ecommerce.modules.user.controllers;

import com.example.ecommerce.controllers.BaseController;
import com.example.ecommerce.enums.PermissionEnum;
import com.example.ecommerce.modules.user.dtos.PermissionDTO;
import com.example.ecommerce.modules.user.dtos.requests.Permission.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.Permission.UpdateRequest;
import com.example.ecommerce.modules.user.services.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Tag(name="Permission API")
@Validated
@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController extends BaseController<PermissionDTO, StoreRequest, UpdateRequest, Long> {

    protected String getEntityName() {
        return "Permission";
    }

    public PermissionController(PermissionService permissionService) {
        super(permissionService, PermissionEnum.PERMISSION);
    }
    
}
