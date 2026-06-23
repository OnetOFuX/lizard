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

import com.lizardstore.auth.dto.UserResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<UserResponse> listAllUsers() {
        return authUserRepository.findAll().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        return authUserRepository.findById(id)
                .map(this::toUserResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public UserResponse changePassword(Long id, String newPassword) {
        AuthUser user = authUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setPassword(passwordEncoder.encode(newPassword));
        authUserRepository.save(user);
        return toUserResponse(user);
    }

    private UserResponse toUserResponse(AuthUser user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .roles(roleNames)
                .build();
    }
}
