package org.ably.circular.user;

import lombok.RequiredArgsConstructor;
import org.ably.circular.role.RoleService;
import org.ably.circular.role.AppRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        String adminEmail = "admin1@circular.com";

        if (!userRepository.findByEmail(adminEmail).isPresent()) {
            User adminUser = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("System")
                    .lastName("Administrator")
                    .phoneNumber("+1234567890")
                    .status(UserStatus.ACTIVE)
                    .createdAt(new Date())
                    .roles(roleService.getRolesByName(AppRole.ADMIN.name()))
                    .build();

            userRepository.save(adminUser);
            System.out.println("Default admin user created successfully!");
        } else {
            System.out.println("Admin user already exists, skipping creation");
        }
    }
}