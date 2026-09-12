package com.hvu46.auth_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private List<String> roles;
}
