package com.springauthapi.authservice.exceptions;

public class MaxTableRecordLimitReached extends RuntimeException {
    public MaxTableRecordLimitReached(String message) {
        super(message);
    }
}
