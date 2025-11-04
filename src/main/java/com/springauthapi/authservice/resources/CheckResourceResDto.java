package com.springauthapi.authservice.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckResourceResDto {
    private String action;
    private Boolean isAuthorized;   
}