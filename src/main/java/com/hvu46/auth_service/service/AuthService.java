package com.hvu46.auth_service.service;

import com.hvu46.auth_service.constants.AuthConstants;
import com.hvu46.auth_service.dto.request.LoginRequestDto;
import com.hvu46.auth_service.dto.request.RegisterRequestDto;
import com.hvu46.auth_service.dto.request.TokenRefreshRequest;
import com.hvu46.auth_service.dto.response.LoginResponseDto;
import com.hvu46.auth_service.entity.RefreshToken;
import com.hvu46.auth_service.entity.Role;
import com.hvu46.auth_service.entity.User;
import com.hvu46.auth_service.repository.RoleRepository;
import com.hvu46.auth_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public LoginResponseDto login(@Valid LoginRequestDto request) {
        log.info("Start check login for: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
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

    public LoginResponseDto refreshAccessToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user);
                    return LoginResponseDto.builder()
                            .accessToken(token)
                            .refreshToken(requestRefreshToken)
                            .tokenType("Bearer")
                            .expiresIn(jwtService.getExpirationTime())
                            .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Refresh Token not exist."));
    }
}
