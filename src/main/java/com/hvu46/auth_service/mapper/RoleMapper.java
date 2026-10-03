package com.hvu46.auth_service.mapper;

import com.hvu46.auth_service.dto.response.RoleResponseDto;
import com.hvu46.auth_service.entity.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {

    RoleResponseDto roleResponseDto(Role role);
}
