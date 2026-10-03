package com.hvu46.auth_service.controller;

import com.hvu46.auth_service.dto.response.UserProfileResponseDto;
import com.hvu46.auth_service.entity.User;
import com.hvu46.auth_service.mapper.UserDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserDetailsMapper userDetailsMapper;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal User user) {
        UserProfileResponseDto response = userDetailsMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }
}
