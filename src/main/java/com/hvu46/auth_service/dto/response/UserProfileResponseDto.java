package com.hvu46.auth_service.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserProfileResponseDto {
    private UUID id;
    private String username;
    private String email;
    private boolean enabled;
    private List<String> roles;
}
