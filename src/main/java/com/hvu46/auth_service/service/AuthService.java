package com.hvu46.auth_service.service;

import com.hvu46.auth_service.constants.AuthConstants;
import com.hvu46.auth_service.dto.request.LoginRequestDto;
import com.hvu46.auth_service.dto.request.RegisterRequestDto;
import com.hvu46.auth_service.dto.response.LoginResponseDto;
import com.hvu46.auth_service.entity.Role;
import com.hvu46.auth_service.entity.User;
import com.hvu46.auth_service.repository.RoleRepository;
import com.hvu46.auth_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(@Valid LoginRequestDto request) {

        return new LoginResponseDto();
    }

    @Transactional
    public void register(@Valid RegisterRequestDto request) {
        log.info("Start register for : {}", request.getUsername());
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username exited");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email existed");
        }

        Role defaultRole = roleRepository.findByName(AuthConstants.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Can't find User role"));

        User newUser = new User();
        newUser.setEmail(request.getEmail().trim());
        newUser.setUsername(request.getUsername().trim());
        newUser.setPassword(
                passwordEncoder.encode(request.getPassword().trim())
        );
        newUser.setRoles(Set.of(defaultRole));

        userRepository.save(newUser);
        log.info("Register success for: {}", request.getUsername());
    }
}
