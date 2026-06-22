package com.lizardstore.auth.service;

import com.lizardstore.auth.config.JwtProperties;
import com.lizardstore.auth.dto.AuthLoginRequest;
import com.lizardstore.auth.dto.AuthLoginResponse;
import com.lizardstore.auth.dto.AuthRegisterRequest;
import com.lizardstore.auth.entity.AuthUser;
import com.lizardstore.auth.entity.Role;
import com.lizardstore.auth.repository.AuthUserRepository;
import com.lizardstore.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthLoginResponse login(AuthLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AuthUser authUser = authUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();
        String token = jwtService.generateToken(userDetails, authUser.getId());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return AuthLoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getExpiration())
                .username(userDetails.getUsername())
                .userId(authUser.getId())
                .roles(roles)
                .build();
    }

    public AuthLoginResponse register(AuthRegisterRequest request) {
        if (authUserRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

        AuthUser newUser = AuthUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(java.util.Set.of(userRole))
                .build();

        authUserRepository.save(newUser);

        AuthLoginRequest loginRequest = new AuthLoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());
        return login(loginRequest);
    }
}
