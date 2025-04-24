package com.example.ecommerce.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ecommerce.config.JwtConfig;
import com.example.ecommerce.modules.user.entities.RefreshToken;
import com.example.ecommerce.modules.user.repositories.BlacklistTokenRepo;
import com.example.ecommerce.modules.user.repositories.RefreshTokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;
    private final Key key;
    private final BlacklistTokenRepo blacklistTokenRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    public JwtUtil(JwtConfig jwtConfig, BlacklistTokenRepo blacklistTokenRepo, RefreshTokenRepo refreshTokenRepo) {
        this.jwtConfig = jwtConfig;
        this.blacklistTokenRepo = blacklistTokenRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes()));
    }

    public String generateToken(Long userId, String email) throws IllegalArgumentException {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpirationTime());

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Long userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getRefreshTokenExpirationTime());

        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // check if this user already has a refresh token
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepo.findByUserId(userId);

        // if user already has a refresh token
        if (optionalRefreshToken.isPresent()) {
            // get the refresh token from database
            RefreshToken dBRefreshToken = optionalRefreshToken.get();

            // update the refresh token from db
            dBRefreshToken.setRefreshToken(refreshToken);
            dBRefreshToken.setExpiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            refreshTokenRepo.save(dBRefreshToken);
            return refreshToken;
        }else {
            // if user does not have a refresh token, create a new one
            RefreshToken token = new RefreshToken();
            token.setRefreshToken(refreshToken);
            token.setUserId(userId);
            token.setExpiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

            refreshTokenRepo.save(token);

            return refreshToken;
        }
    }

    /*
        1. token is valid or not
        2. Signature is valid or not
        3. token is expired or not
        4. check the issuer of token
        5. email of token and email of UserDetails is same or not
        6. token is in blacklist or not
        7. check the role of token
     */
    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC512(Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes()));

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(jwtConfig.getIssuer())
                .build();

        // automatically verify signature, expiration and issuer
        DecodedJWT jwt = verifier.verify(token);

        // other verifications
//        String tokenEmail = jwt.getClaim("email").asString();
//        if (!tokenEmail.equals(userDetails.getUsername()))
//            throw new JWTVerificationException("Token email does not match user email");
        if (isBlacklistToken(token))
            throw new JWTVerificationException("Token is locked");

        return jwt;
    }

    public String getUserIdFromToken(String token) {
        return verifyToken(token).getSubject();
    }

    public boolean isBlacklistToken(String token) {
        return blacklistTokenRepo.existsByToken(token);
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            return refreshTokenRepo.existsByRefreshToken(refreshToken);
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    // check token is valid or not
//    public boolean isTokenFormatValid(String token) {
//        try {
//            String[] tokenParts = token.split("\\.");
//
//            return tokenParts.length == 3;
//
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // check signature is valid or not
//    public boolean isTokenSignatureValid(String  token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token);
//
//            return true;
//
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // check token is expired or not
//    public boolean isTokenExpired(String token) {
//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .getExpiration()
//                    .after(new Date());
//
//        } catch (Exception e) {
//            return true;
//        }
//    }
//
//    // check issuer of token is valid or not
//    public boolean isIssuerValid(String token) {
//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .getIssuer()
//                    .equals(jwtConfig.getIssuer());
//
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // check email of token and email of UserDetails is same or not
//    public boolean isEmailValid(String token){
//        try {
//            String emailToken = getEmailFromToken(token);
//            return emailToken.equals(userDetails.getUsername());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes()));
//    }
}
