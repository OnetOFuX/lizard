package com.lizardstore.auth.controller;

import com.lizardstore.auth.dto.AuthLoginRequest;
import com.lizardstore.auth.dto.AuthLoginResponse;
import com.lizardstore.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthLoginResponse> register(@Valid @RequestBody com.lizardstore.auth.dto.AuthRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
