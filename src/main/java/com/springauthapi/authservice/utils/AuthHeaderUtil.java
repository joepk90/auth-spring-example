package com.springauthapi.authservice.utils;

import jakarta.servlet.http.HttpServletRequest;

public final class AuthHeaderUtil {

    // prevent instantiation
    private AuthHeaderUtil() {} 

    public static String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.replace("Bearer ", "");
    }
}
