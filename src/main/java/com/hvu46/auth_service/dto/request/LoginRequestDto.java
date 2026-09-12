package com.hvu46.auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotBlank(message = "Password must not be blank")
    private String password;
}
