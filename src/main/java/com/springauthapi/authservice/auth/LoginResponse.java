package com.springauthapi.authservice.auth;

import com.springauthapi.authservice.jwt.Jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private Jwt accessToken;
    private Jwt refreshToken;
}