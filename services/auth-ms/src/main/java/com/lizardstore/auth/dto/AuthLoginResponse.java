package com.lizardstore.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AuthLoginResponse {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private String username;
    private Long userId;
    private List<String> roles;
}
