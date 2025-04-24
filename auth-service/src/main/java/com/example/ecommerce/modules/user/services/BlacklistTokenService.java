package com.example.ecommerce.modules.user.services;

import com.example.ecommerce.modules.user.dtos.requests.TokenRequest;

public interface BlacklistTokenService {
    void create(TokenRequest blacklistTokenRequest);
}
