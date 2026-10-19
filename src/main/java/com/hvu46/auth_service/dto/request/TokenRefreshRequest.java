package com.hvu46.auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh Token must not be blank")
    private String refreshToken;
}
