package com.lizardstore.auth.controller;

import com.lizardstore.auth.dto.AuthLoginRequest;
import com.lizardstore.auth.dto.AuthLoginResponse;
import com.lizardstore.auth.dto.UserResponse;
import com.lizardstore.auth.service.AuthService;
import com.lizardstore.auth.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthLoginResponse> register(@Valid @RequestBody com.lizardstore.auth.dto.AuthRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> listarUsuarios(
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<String> roles = jwtService.extractRoles(token);
        if (roles == null || !roles.contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(authService.listAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> buscarUsuarioPorId(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long tokenUserId = jwtService.extractUserId(token);
        List<String> roles = jwtService.extractRoles(token);

        if (tokenUserId == null || (!tokenUserId.equals(id) && (roles == null || !roles.contains("ROLE_ADMIN")))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(authService.getUserById(id));
    }

    @PostMapping("/users/{id}/change-password")
    public ResponseEntity<UserResponse> cambiarContrasena(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long tokenUserId = jwtService.extractUserId(token);
        if (tokenUserId == null || !tokenUserId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(authService.changePassword(id, newPassword));
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<UserResponse> actualizarEstadoUsuario(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body,
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<String> roles = jwtService.extractRoles(token);
        if (roles == null || !roles.contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Boolean enabled = body.get("enabled");
        if (enabled == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(authService.updateUserStatus(id, enabled));
    }

    @PutMapping("/users/{id}/roles")
    public ResponseEntity<UserResponse> actualizarRolesUsuario(
            @PathVariable Long id,
            @RequestBody Map<String, List<String>> body,
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<String> roles = jwtService.extractRoles(token);
        if (roles == null || !roles.contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<String> roleNames = body.get("roles");
        if (roleNames == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(authService.updateUserRoles(id, new java.util.HashSet<>(roleNames)));
    }
}
