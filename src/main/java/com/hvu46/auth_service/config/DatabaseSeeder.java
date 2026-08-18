package com.hvu46.auth_service.config;

import com.hvu46.auth_service.constants.AuthConstants;
import com.hvu46.auth_service.entity.Role;
import com.hvu46.auth_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private record RoleDef(String name, String description) {}

    private static final List<RoleDef> ROLE_DEFINITIONS = List.of(
            new RoleDef(AuthConstants.ROLE_USER, "Default user of system"),
            new RoleDef(AuthConstants.ROLE_ADMIN, "Admin of system")
    );

    @Override
    public void run(String... args) throws Exception {
        log.info("Start initiate data for Roles");

        for (RoleDef def : ROLE_DEFINITIONS) {
            roleRepository.findByName(def.name()).orElseGet(() -> {
                Role role = new Role();
                role.setName(def.name());
                role.setDescription(def.description());
                log.info("Created role: {}", def.name());
                return roleRepository.save(role);
            });
        }

        log.info("Init data done");
    }
}
