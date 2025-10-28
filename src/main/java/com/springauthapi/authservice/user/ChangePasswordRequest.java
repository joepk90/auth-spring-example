package com.springauthapi.authservice.user;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String NewPassword;
}
