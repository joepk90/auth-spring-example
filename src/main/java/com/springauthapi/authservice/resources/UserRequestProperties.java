package com.springauthapi.authservice.resources;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestProperties {
    private String userId;
    private String role;
}