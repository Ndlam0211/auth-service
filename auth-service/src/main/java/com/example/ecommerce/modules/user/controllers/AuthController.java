package com.example.ecommerce.modules.user.controllers;

import com.example.ecommerce.dtos.APIResponse;
import com.example.ecommerce.dtos.MessageResponse;
import com.example.ecommerce.modules.user.dtos.requests.TokenRequest;
import com.example.ecommerce.modules.user.dtos.requests.LoginRequest;
import com.example.ecommerce.modules.user.dtos.responses.LoginResponse;
import com.example.ecommerce.modules.user.dtos.responses.RefreshTokenResponse;
import com.example.ecommerce.modules.user.services.BlacklistTokenService;
import com.example.ecommerce.modules.user.services.UserService;
import com.example.ecommerce.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@Tag(name="Auth API")
@Validated
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final BlacklistTokenService blacklistTokenService;

    public AuthController(JwtUtil jwtUtil, UserService userService, BlacklistTokenService blacklistTokenService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.blacklistTokenService = blacklistTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        APIResponse<LoginResponse> auth = userService.authenticate(loginRequest);

        return ResponseEntity.ok(auth);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken){
        try {
            userService.logout(bearerToken);
            return ResponseEntity.ok( APIResponse.message("Logout successfully",HttpStatus.OK));
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(APIResponse.error("LOGOUT_ERROR","Logout failed",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/blacklist_token")
    public ResponseEntity<?> addTokenToBlacklist(@Valid @RequestBody TokenRequest request){
            blacklistTokenService.create(request);
            return ResponseEntity.ok( APIResponse.message("Added token to blacklist successfully",HttpStatus.OK));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String bearerToken){
        String refreshToken = bearerToken.substring(7);

        if(!jwtUtil.isRefreshTokenValid(refreshToken))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Refresh token is invalid"));

        Claims claims = jwtUtil.getAllClaimsFromToken(refreshToken);
        String userId = claims.getSubject();
        String email = claims.get("email", String.class);

        String token = jwtUtil.generateToken(Long.valueOf(userId), email);
        String newRefreshToken = jwtUtil.generateRefreshToken(Long.valueOf(userId), email);

        RefreshTokenResponse tokenResponse = new RefreshTokenResponse(token, newRefreshToken);
        APIResponse<RefreshTokenResponse> response = APIResponse.ok(tokenResponse, "Token refreshed successfully");
        return ResponseEntity.ok(response);
    }

}
