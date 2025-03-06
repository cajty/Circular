package org.ably.circular.role;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.Set;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
       Set<String> defaultRoles = Set.of(
         "ADMIN", "USER", "MANAGER", "EMPLOYEE"
       );

        for (String roleName : defaultRoles) {
            if (!roleRepository.existsByName(roleName)) {
                Role newRole = Role.builder().name(roleName).build();
                roleRepository.save(newRole);
            }
        }

        System.out.println("Default roles inserted successfully!");
    }
}