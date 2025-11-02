package com.springauthapi.authservice.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckResourceResponse {
    private String action;
    private Boolean isAuthorized;   
}