package com.lizardstore.auth.config;

import com.lizardstore.auth.entity.AuthUser;
import com.lizardstore.auth.entity.Role;
import com.lizardstore.auth.repository.AuthUserRepository;
import com.lizardstore.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initAuthData() {
        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));

            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));

            if (!authUserRepository.existsByUsername("admin")) {
                authUserRepository.save(AuthUser.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .enabled(true)
                        .roles(Set.of(adminRole))
                        .build());
            }

            if (!authUserRepository.existsByUsername("user")) {
                authUserRepository.save(AuthUser.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user123"))
                        .enabled(true)
                        .roles(Set.of(userRole))
                        .build());
            }
        };
    }
}
