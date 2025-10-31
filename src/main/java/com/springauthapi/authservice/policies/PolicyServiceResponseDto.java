package com.springauthapi.authservice.policies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PolicyServiceResponseDto {
    private String action;
    private Boolean isAuthorized;   
}