package com.example.ecommerce.modules.user.services;

import com.example.ecommerce.modules.user.dtos.UserCatalogueDTO;
import com.example.ecommerce.modules.user.dtos.requests.UserCatalogue.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.UserCatalogue.UpdateRequest;
import com.example.ecommerce.services.BaseServiceInterface;

public interface UserCatalogueService extends BaseServiceInterface<UserCatalogueDTO, StoreRequest, UpdateRequest, Long> {}
