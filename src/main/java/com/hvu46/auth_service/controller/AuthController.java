package com.hvu46.auth_service.controller;

import com.hvu46.auth_service.dto.request.LoginRequestDto;
import com.hvu46.auth_service.dto.request.LogoutRequest;
import com.hvu46.auth_service.dto.request.RegisterRequestDto;
import com.hvu46.auth_service.dto.request.TokenRefreshRequest;
import com.hvu46.auth_service.dto.response.LoginResponseDto;
import com.hvu46.auth_service.service.AuthService;
import com.hvu46.auth_service.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDto request) {
        try {
            authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Register successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("System error!");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshAccessToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Valid @RequestBody LogoutRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader
            ) {
        authService.logout(request, authHeader);
        return ResponseEntity.ok("Logout success");
    }
}
