package com.hvu46.auth_service.dto.response;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String status;
    private String token;
}
