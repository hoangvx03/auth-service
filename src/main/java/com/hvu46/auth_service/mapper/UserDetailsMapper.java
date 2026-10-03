package com.hvu46.auth_service.mapper;

import com.hvu46.auth_service.dto.response.UserProfileResponseDto;
import com.hvu46.auth_service.entity.Role;
import com.hvu46.auth_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {

    UserProfileResponseDto toResponse(User user);

    default String mapRoleToString(Role role) {
        if (role == null)   return null;
        return role.getName();
    }
}
