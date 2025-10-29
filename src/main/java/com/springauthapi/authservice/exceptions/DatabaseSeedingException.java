package com.springauthapi.authservice.exceptions;

public class DatabaseSeedingException extends RuntimeException {
    public DatabaseSeedingException(String message) {
        super(message);
    }
}