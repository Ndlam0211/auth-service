package com.example.ecommerce.modules.user.services.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ecommerce.dtos.MessageResponse;
import com.example.ecommerce.exceptions.ResourceExistedException;
import com.example.ecommerce.modules.user.dtos.requests.TokenRequest;
import com.example.ecommerce.modules.user.entities.BlacklistToken;
import com.example.ecommerce.modules.user.repositories.BlacklistTokenRepo;
import com.example.ecommerce.modules.user.services.BlacklistTokenService;
import com.example.ecommerce.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Service
public class BlacklistTokenServiceImpl implements BlacklistTokenService {
    @Autowired
    private BlacklistTokenRepo blacklistTokenRepo;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(BlacklistTokenServiceImpl.class);

    public void create(TokenRequest request) {
           if(blacklistTokenRepo.existsByToken(request.getToken())){
               throw new ResourceExistedException("Token blacklist", "token", request.getToken());
           }

            // validate token and obtain a DecodedJWT contains claims from token if token is valid if not throw JWTVerificationException
            DecodedJWT jwt = jwtUtil.verifyToken(request.getToken());

            Long userId = Long.valueOf(jwt.getSubject());
            Date expiryDate = jwt.getExpiresAt();

            BlacklistToken blacklistToken = new BlacklistToken();
            blacklistToken.setToken(request.getToken());
            blacklistToken.setExpiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            blacklistToken.setUserId(userId);

            blacklistTokenRepo.save(blacklistToken);
            logger.info("Added token to blacklist successfully");
    }
}
